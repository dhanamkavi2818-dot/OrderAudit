
package com.foodfactory.orderaudit;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orders")
public class SalesOrderController {

    private final SalesOrderService salesOrderService;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public SalesOrderController(
            SalesOrderService salesOrderService,
            CustomerRepository customerRepository,
            ProductRepository productRepository) {

        this.salesOrderService = salesOrderService;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    // DISPLAY ALL SALES ORDERS

    @GetMapping
    public String showOrders(Model model) {

        model.addAttribute(
                "orders", salesOrderService.getAllOrders());

        model.addAttribute(
                "customers", customerRepository.findAll());

        model.addAttribute(
                "products", productRepository.findAll());

        return "orders";
    }

    // CREATE NEW SALES ORDER

    @PostMapping("/save")
    public String saveOrder(
            @RequestParam Long customerId,
            @RequestParam Long productId,
            @RequestParam Integer quantity,
            Model model) {

        try {

            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Customer not found"));

            Product product = productRepository.findById(productId)
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Product not found"));

            SalesOrder order = new SalesOrder();

            order.setCustomer(customer);
            order.setProduct(product);
            order.setQuantity(quantity);

            salesOrderService.createOrder(order);

            return "redirect:/orders?success";

        } catch (IllegalArgumentException e) {

            model.addAttribute("error", e.getMessage());

            return showOrders(model);
        }
    }

    // APPROVE SALES ORDER

    @PostMapping("/approve/{id}")
    public String approveOrder(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {

            salesOrderService.approveOrder(id);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Order approved successfully!");

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage());
        }

        return "redirect:/orders";
    }

@PostMapping("/dispatch/{id}")
public String dispatchOrder(
        @PathVariable Long id,
        RedirectAttributes redirectAttributes) {

    try {

        salesOrderService.dispatchOrder(id);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Order dispatched successfully!");

    } catch (IllegalArgumentException e) {

        redirectAttributes.addFlashAttribute(
                "error",
                e.getMessage());
    }

    return "redirect:/orders";
}

    // UPDATE QUANTITY OF A PENDING ORDER

    @PostMapping("/update/{id}")
    public String updateOrder(
            @PathVariable Long id,
            @RequestParam Integer quantity,
            RedirectAttributes redirectAttributes) {

        try {

            salesOrderService.updateOrderQuantity(
                    id, quantity);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Order quantity updated successfully!");

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage());
        }

        return "redirect:/orders";
    }

}
