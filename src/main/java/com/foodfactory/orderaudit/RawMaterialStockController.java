
package com.foodfactory.orderaudit;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/stock")
public class RawMaterialStockController {

    private final RawMaterialService rawMaterialService;
    private final RawMaterialStockTransactionService stockService;

    public RawMaterialStockController(
            RawMaterialService rawMaterialService,
            RawMaterialStockTransactionService stockService) {
        this.rawMaterialService = rawMaterialService;
        this.stockService = stockService;
    }

    @GetMapping
    public String showStock(Model model) {

        var materials = rawMaterialService.getAllRawMaterials();

        Map<Long, BigDecimal> stockBalances = new HashMap<>();

        for (RawMaterial material : materials) {
            stockBalances.put(
                    material.getId(),
                    stockService.getAvailableStock(material.getId())
            );
        }

        model.addAttribute("materials", materials);
        model.addAttribute("stockBalances", stockBalances);
        model.addAttribute("transactions",
                stockService.getAllTransactions());

        return "stock";
    }

    @PostMapping("/save")
    public String saveTransaction(
            @RequestParam Long rawMaterialId,
            @RequestParam String transactionType,
            @RequestParam BigDecimal quantity,
            @RequestParam(required = false) BigDecimal unitCost,
            @RequestParam(required = false) String reference) {

        RawMaterial material =
                rawMaterialService.getRawMaterialById(rawMaterialId);

        if (material != null && quantity.compareTo(BigDecimal.ZERO) > 0) {

            RawMaterialStockTransaction transaction =
                    new RawMaterialStockTransaction();

            transaction.setRawMaterial(material);
            transaction.setTransactionType(transactionType);
            transaction.setQuantity(quantity);
            transaction.setUnitCost(unitCost);
            transaction.setReference(reference);
            transaction.setTransactionDate(LocalDateTime.now());

            stockService.saveTransaction(transaction);
        }

        return "redirect:/stock";
    }
}
