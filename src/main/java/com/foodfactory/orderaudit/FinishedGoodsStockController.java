
package com.foodfactory.orderaudit;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FinishedGoodsStockController {

    private final FinishedGoodsStockTransactionService stockService;
    private final ProductRepository productRepository;

    public FinishedGoodsStockController(
            FinishedGoodsStockTransactionService stockService,
            ProductRepository productRepository) {

        this.stockService = stockService;
        this.productRepository = productRepository;
    }

    @GetMapping("/finished-stock")
    public String showFinishedStock(Model model) {

        List<Product> products = productRepository.findAll();

        Map<Long, Integer> availableStock = new HashMap<>();

        for (Product product : products) {
            availableStock.put(
                    product.getId(),
                    stockService.getAvailableStock(product.getId())
            );
        }

        model.addAttribute("products", products);
        model.addAttribute("availableStock", availableStock);

        model.addAttribute(
                "transactions",
                stockService.getAllTransactions()
        );

        return "finished-stock";
    }
}
