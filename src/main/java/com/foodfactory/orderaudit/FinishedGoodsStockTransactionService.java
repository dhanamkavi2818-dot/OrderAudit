
package com.foodfactory.orderaudit;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FinishedGoodsStockTransactionService {

    private final FinishedGoodsStockTransactionRepository repository;

    public FinishedGoodsStockTransactionService(
            FinishedGoodsStockTransactionRepository repository) {
        this.repository = repository;
    }

    public FinishedGoodsStockTransaction saveTransaction(
            FinishedGoodsStockTransaction transaction) {
        return repository.save(transaction);
    }

    public List<FinishedGoodsStockTransaction> getAllTransactions() {
        return repository.findAll();
    }

    public int getAvailableStock(Long productId) {

        int availableStock = 0;

        List<FinishedGoodsStockTransaction> transactions =
                repository.findByProductId(productId);

        for (FinishedGoodsStockTransaction transaction : transactions) {

            String type = transaction.getTransactionType();
            int quantity = transaction.getQuantity();

            if ("PRODUCTION".equals(type)
                    || "OPENING_STOCK".equals(type)
                    || "ADJUSTMENT_IN".equals(type)) {

                availableStock += quantity;

            } else if ("DISPATCH".equals(type)
                    || "ADJUSTMENT_OUT".equals(type)) {

                availableStock -= quantity;
            }
        }

        return availableStock;
    }
}
