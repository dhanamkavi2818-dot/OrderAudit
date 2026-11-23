
package com.foodfactory.orderaudit;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;

@Controller
public class HomeController {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentService paymentService;

    public HomeController(
            CustomerRepository customerRepository,
            ProductRepository productRepository,
            SalesOrderRepository salesOrderRepository,
            InvoiceRepository invoiceRepository,
            PaymentService paymentService) {

        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.salesOrderRepository = salesOrderRepository;
        this.invoiceRepository = invoiceRepository;
        this.paymentService = paymentService;
    }

    @GetMapping("/")
    public String home(Model model) {

        // Count customers, products, orders and invoices
        model.addAttribute(
                "totalCustomers", customerRepository.count());

        model.addAttribute(
                "totalProducts", productRepository.count());

        model.addAttribute(
                "totalOrders", salesOrderRepository.count());

        model.addAttribute(
                "totalInvoices", invoiceRepository.count());

        // Calculate total payments received
        BigDecimal totalPayments = BigDecimal.ZERO;

        for (Payment payment : paymentService.getAllPayments()) {
            totalPayments = totalPayments.add(payment.getAmount());
        }

        model.addAttribute("totalPayments", totalPayments);

        // Calculate total outstanding balance
        BigDecimal totalOutstanding = BigDecimal.ZERO;

        for (Invoice invoice : invoiceRepository.findAll()) {

            totalOutstanding = totalOutstanding.add(
                    paymentService.getOutstandingBalance(
                            invoice.getId()));
        }

        model.addAttribute("totalOutstanding", totalOutstanding);

        return "index";
    }
}
