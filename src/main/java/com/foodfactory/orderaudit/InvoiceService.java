
package com.foodfactory.orderaudit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final SalesOrderRepository salesOrderRepository;

    public InvoiceService(
            InvoiceRepository invoiceRepository,
            SalesOrderRepository salesOrderRepository) {

        this.invoiceRepository = invoiceRepository;
        this.salesOrderRepository = salesOrderRepository;
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Invoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invoice not found"));
    }

    @Transactional
    public Invoice generateInvoice(Long orderId) {

        SalesOrder order = salesOrderRepository.findById(orderId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Sales order not found"));

        // Only dispatched orders can be invoiced
        if (!"DISPATCHED".equals(order.getStatus())) {
            throw new IllegalArgumentException(
                    "Only dispatched orders can be invoiced");
        }

        // Prevent duplicate invoices
        if (invoiceRepository.existsBySalesOrderId(orderId)) {
            throw new IllegalArgumentException(
                    "Invoice already exists for this order");
        }

        Product product = order.getProduct();
        Customer customer = order.getCustomer();

        if (product.getSellingPrice() == null ||
                product.getGstRate() == null) {
            throw new IllegalArgumentException(
                    "Product price and GST rate are required");
        }

        if (customer.getState() == null ||
                customer.getState().isBlank()) {
            throw new IllegalArgumentException(
                    "Customer state is required");
        }

        BigDecimal quantity =
                BigDecimal.valueOf(order.getQuantity());

        BigDecimal taxableAmount = product.getSellingPrice()
                .multiply(quantity)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal gstRate = product.getGstRate();

        BigDecimal totalGst = taxableAmount
                .multiply(gstRate)
                .divide(BigDecimal.valueOf(100), 2,
                        RoundingMode.HALF_UP);

        BigDecimal cgst = BigDecimal.ZERO.setScale(2);
        BigDecimal sgst = BigDecimal.ZERO.setScale(2);
        BigDecimal igst = BigDecimal.ZERO.setScale(2);

        // Factory is located in Tamil Nadu
        if ("Tamil Nadu".equalsIgnoreCase(
                customer.getState().trim())) {

            BigDecimal halfRate = gstRate.divide(
                    BigDecimal.valueOf(2));

            cgst = taxableAmount.multiply(halfRate)
                    .divide(BigDecimal.valueOf(100), 2,
                            RoundingMode.HALF_UP);

            sgst = totalGst.subtract(cgst);

        } else {

            igst = totalGst;
        }

        BigDecimal totalAmount = taxableAmount
                .add(cgst)
                .add(sgst)
                .add(igst);

        Invoice invoice = new Invoice();

        invoice.setSalesOrder(order);
        invoice.setInvoiceNumber("INV-" + order.getId());
        invoice.setTaxableAmount(taxableAmount);
        invoice.setCgstAmount(cgst);
        invoice.setSgstAmount(sgst);
        invoice.setIgstAmount(igst);
        invoice.setTotalAmount(totalAmount);

        return invoiceRepository.save(invoice);
    }
}
