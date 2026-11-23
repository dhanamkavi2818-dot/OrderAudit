
package com.foodfactory.orderaudit;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final InvoiceService invoiceService;

    public PaymentController(
            PaymentService paymentService,
            InvoiceService invoiceService) {

        this.paymentService = paymentService;
        this.invoiceService = invoiceService;
    }

    // Display payments and outstanding balances
    @GetMapping
    public String showPayments(Model model) {

        model.addAttribute(
                "payments", paymentService.getAllPayments());

        model.addAttribute(
                "invoices", invoiceService.getAllInvoices());

        Map<Long, BigDecimal> outstandingBalances =
                new HashMap<>();

        for (Invoice invoice : invoiceService.getAllInvoices()) {

            outstandingBalances.put(
                    invoice.getId(),
                    paymentService.getOutstandingBalance(
                            invoice.getId()));
        }

        model.addAttribute(
                "outstandingBalances", outstandingBalances);

        return "payments";
    }

    // Record a customer payment
    @PostMapping("/save")
    public String savePayment(
            @RequestParam Long invoiceId,
            @RequestParam BigDecimal amount,
            @RequestParam String paymentMode,
            @RequestParam(required = false)
                    String referenceNumber,
            RedirectAttributes redirectAttributes) {

        try {

            paymentService.recordPayment(
                    invoiceId,
                    amount,
                    paymentMode,
                    referenceNumber);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Payment recorded successfully!");

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage());
        }

        return "redirect:/payments";
    }
}
