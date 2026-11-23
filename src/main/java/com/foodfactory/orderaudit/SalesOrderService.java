
package com.foodfactory.orderaudit;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
@Service
public class SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final FinishedGoodsStockTransactionService finishedGoodsService;

    
public SalesOrderService(
        SalesOrderRepository salesOrderRepository,
        FinishedGoodsStockTransactionService finishedGoodsService) {

    this.salesOrderRepository = salesOrderRepository;
    this.finishedGoodsService = finishedGoodsService;
}


    public List<SalesOrder> getAllOrders() {
        return salesOrderRepository.findAll();
    }

    public SalesOrder getOrderById(Long id) {
        return salesOrderRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Sales order not found"));
    }

    public SalesOrder createOrder(SalesOrder order) {

        if (order.getCustomer() == null ||
                order.getProduct() == null) {
            throw new IllegalArgumentException(
                    "Customer and product are required");
        }

        if (order.getQuantity() == null ||
                order.getQuantity() <= 0) {
            throw new IllegalArgumentException(
                    "Order quantity must be greater than zero");
        }

        order.setStatus("PENDING");
        order.setOrderDate(LocalDateTime.now());

        return salesOrderRepository.save(order);
    }

public SalesOrder approveOrder(Long orderId) {

    SalesOrder order = getOrderById(orderId);

    if (!"PENDING".equals(order.getStatus())) {
        throw new IllegalArgumentException(
                "Only pending orders can be approved");
    }

    int availableStock = finishedGoodsService
            .getAvailableStock(order.getProduct().getId());

    if (availableStock < order.getQuantity()) {
        throw new IllegalArgumentException(
                "Insufficient finished goods stock. Available: "
                + availableStock
                + " packets, Requested: "
                + order.getQuantity()
                + " packets");
    }

    order.setStatus("APPROVED");

    return salesOrderRepository.save(order);
}

@Transactional
public SalesOrder dispatchOrder(Long orderId) {

    SalesOrder order = getOrderById(orderId);

    // Only approved orders can be dispatched
    if (!"APPROVED".equals(order.getStatus())) {
        throw new IllegalArgumentException(
                "Only approved orders can be dispatched");
    }

    // Check finished goods stock
    int availableStock = finishedGoodsService
            .getAvailableStock(order.getProduct().getId());

    if (availableStock < order.getQuantity()) {
        throw new IllegalArgumentException(
                "Insufficient finished goods stock. Available: "
                + availableStock
                + ", Requested: "
                + order.getQuantity());
    }

    // Create finished goods dispatch transaction
    FinishedGoodsStockTransaction transaction =
            new FinishedGoodsStockTransaction();

    transaction.setProduct(order.getProduct());
    transaction.setTransactionType("DISPATCH");
    transaction.setQuantity(order.getQuantity());
    transaction.setReference("SALES-ORDER-" + order.getId());

    finishedGoodsService.saveTransaction(transaction);

    // Update sales order status
    order.setStatus("DISPATCHED");

    return salesOrderRepository.save(order);
}


    // UPDATE QUANTITY OF A PENDING ORDER

    @Transactional
    public SalesOrder updateOrderQuantity(
            Long orderId, Integer quantity) {

        SalesOrder order = getOrderById(orderId);

        // Only pending orders can be edited
        if (!"PENDING".equals(order.getStatus())) {
            throw new IllegalArgumentException(
                    "Only pending orders can be edited");
        }

        // Quantity must be positive
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException(
                    "Order quantity must be greater than zero");
        }

        // Update the order quantity
        order.setQuantity(quantity);

        return salesOrderRepository.save(order);
    }

}
