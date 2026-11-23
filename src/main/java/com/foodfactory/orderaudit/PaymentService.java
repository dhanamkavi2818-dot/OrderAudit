
package com.foodfactory.orderaudit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            InvoiceRepository invoiceRepository) {

        this.paymentRepository = paymentRepository;
        this.invoiceRepository = invoiceRepository;
    }

    // Get all payments
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // Calculate total amount paid for an invoice
    public BigDecimal getTotalPaid(Long invoiceId) {

        List<Payment> payments =
                paymentRepository.findByInvoiceId(invoiceId);

        BigDecimal totalPaid = BigDecimal.ZERO;

        for (Payment payment : payments) {
            totalPaid = totalPaid.add(payment.getAmount());
        }

        return totalPaid;
    }

    // Calculate outstanding balance
    public BigDecimal getOutstandingBalance(Long invoiceId) {

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invoice not found"));

        return invoice.getTotalAmount()
                .subtract(getTotalPaid(invoiceId));
    }

    // Record a new payment
    @Transactional
    public Payment recordPayment(
            Long invoiceId,
            BigDecimal amount,
            String paymentMode,
            String referenceNumber) {

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invoice not found"));

        // Validate payment amount
        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Payment amount must be greater than zero");
        }

        // Check outstanding balance
        BigDecimal outstanding =
                getOutstandingBalance(invoiceId);

        if (amount.compareTo(outstanding) > 0) {

            throw new IllegalArgumentException(
                    "Payment exceeds outstanding balance of Rs. "
                    + outstanding);
        }

        // Save payment
        Payment payment = new Payment();

        payment.setInvoice(invoice);
        payment.setAmount(amount);
        payment.setPaymentMode(paymentMode);
        payment.setReferenceNumber(referenceNumber);

        return paymentRepository.save(payment);
    }
}
