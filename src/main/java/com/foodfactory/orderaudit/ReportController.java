
package com.foodfactory.orderaudit;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final InvoiceService invoiceService;
    private final RawMaterialService rawMaterialService;
    private final RawMaterialStockTransactionService rawMaterialStockService;
    private final FinishedGoodsStockTransactionService finishedGoodsStockService;
    private final ProductRepository productRepository;

    // CONSTRUCTOR

    public ReportController(
            InvoiceService invoiceService,
            RawMaterialService rawMaterialService,
            RawMaterialStockTransactionService rawMaterialStockService,
            FinishedGoodsStockTransactionService finishedGoodsStockService,
            ProductRepository productRepository) {

        this.invoiceService = invoiceService;
        this.rawMaterialService = rawMaterialService;
        this.rawMaterialStockService = rawMaterialStockService;
        this.finishedGoodsStockService = finishedGoodsStockService;
        this.productRepository = productRepository;
    }

    // DISPLAY REPORTS PAGE

    @GetMapping
    public String showReports() {
        return "reports";
    }

    // DOWNLOAD SALES REGISTER AS CSV

    @GetMapping("/sales/csv")
    public ResponseEntity<byte[]> downloadSalesCsv() {

        List<Invoice> invoices = invoiceService.getAllInvoices();

        StringBuilder csv = new StringBuilder();

        csv.append(
            "Invoice Number,Invoice Date,Customer,State," +
            "Product,Quantity,Taxable Amount,CGST,SGST,IGST,Total Amount\n"
        );

        for (Invoice invoice : invoices) {

            SalesOrder order = invoice.getSalesOrder();

            csv.append(escapeCsv(invoice.getInvoiceNumber())).append(",");
            csv.append(escapeCsv(invoice.getInvoiceDate())).append(",");
            csv.append(escapeCsv(order.getCustomer().getName())).append(",");
            csv.append(escapeCsv(order.getCustomer().getState())).append(",");
            csv.append(escapeCsv(order.getProduct().getProductName())).append(",");
            csv.append(order.getQuantity()).append(",");
            csv.append(invoice.getTaxableAmount()).append(",");
            csv.append(invoice.getCgstAmount()).append(",");
            csv.append(invoice.getSgstAmount()).append(",");
            csv.append(invoice.getIgstAmount()).append(",");
            csv.append(invoice.getTotalAmount()).append("\n");
        }

        return createCsvResponse(csv, "sales-register.csv");
    }

    // DOWNLOAD STOCK REPORT AS CSV

    @GetMapping("/stock/csv")
    public ResponseEntity<byte[]> downloadStockCsv() {

        StringBuilder csv = new StringBuilder();

        csv.append("Type,Item Name,Unit or Pack Size,Available Stock\n");

        // RAW MATERIAL STOCK

        List<RawMaterial> rawMaterials =
                rawMaterialService.getAllRawMaterials();

        for (RawMaterial material : rawMaterials) {

            csv.append(escapeCsv("Raw Material")).append(",");
            csv.append(escapeCsv(material.getMaterialName())).append(",");
            csv.append(escapeCsv(material.getUnit())).append(",");
            csv.append(
                rawMaterialStockService.getAvailableStock(material.getId())
            ).append("\n");
        }

        // FINISHED GOODS STOCK

        List<Product> products = productRepository.findAll();

        for (Product product : products) {

            csv.append(escapeCsv("Finished Product")).append(",");
            csv.append(escapeCsv(product.getProductName())).append(",");
            csv.append(escapeCsv(product.getPackSize())).append(",");
            csv.append(
                finishedGoodsStockService.getAvailableStock(product.getId())
            ).append("\n");
        }

        return createCsvResponse(csv, "stock-report.csv");
    }

    // CREATE CSV DOWNLOAD RESPONSE

    private ResponseEntity<byte[]> createCsvResponse(
            StringBuilder csv, String filename) {

        byte[] csvBytes =
                csv.toString().getBytes(StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + filename + "\""
                )
                .contentType(
                    MediaType.parseMediaType("text/csv;charset=UTF-8")
                )
                .body(csvBytes);
    }

    // HANDLE COMMAS, QUOTES AND LINE BREAKS IN CSV VALUES

    private String escapeCsv(Object value) {

        if (value == null) {
            return "";
        }

        String text = value.toString();

        // Prevent spreadsheet formula execution
        if (!text.isEmpty() &&
            ("=+-@\t\r".indexOf(text.charAt(0)) >= 0)) {
            text = "'" + text;
        }

        return "\"" + text.replace("\"", "\"\"") + "\"";
    }
}
