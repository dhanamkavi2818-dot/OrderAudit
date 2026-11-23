
package com.foodfactory.orderaudit;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


@Controller
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final SalesOrderService salesOrderService;
    private final InvoicePdfService invoicePdfService;

    
public InvoiceController(
        InvoiceService invoiceService,
        SalesOrderService salesOrderService,
        InvoicePdfService invoicePdfService) {

    this.invoiceService = invoiceService;
    this.salesOrderService = salesOrderService;
    this.invoicePdfService = invoicePdfService;
}


    // DISPLAY ALL INVOICES

    @GetMapping
    public String showInvoices(Model model) {

        model.addAttribute(
                "invoices", invoiceService.getAllInvoices());

        model.addAttribute(
                "orders", salesOrderService.getAllOrders());

        return "invoices";
    }

    // GENERATE GST INVOICE

    @PostMapping("/generate/{orderId}")
    public String generateInvoice(
            @PathVariable Long orderId,
            RedirectAttributes redirectAttributes) {

        try {

            invoiceService.generateInvoice(orderId);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "GST invoice generated successfully!");

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage());
        }

        return "redirect:/invoices";
    }

    // VIEW INDIVIDUAL INVOICE

    @GetMapping("/{id}")
    public String viewInvoice(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "invoice", invoiceService.getInvoiceById(id));

        return "invoice-details";
    }

    // DOWNLOAD GST INVOICE AS PDF

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadInvoicePdf(
            @PathVariable Long id) {

        Invoice invoice = invoiceService.getInvoiceById(id);

        byte[] pdf = invoicePdfService.generatePdf(invoice);

        String filename = "Invoice-" + invoice.getId() + ".pdf";

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

}
