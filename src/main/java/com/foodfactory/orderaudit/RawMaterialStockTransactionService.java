
package com.foodfactory.orderaudit;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class RawMaterialStockTransactionService {

    private final RawMaterialStockTransactionRepository repository;

    public RawMaterialStockTransactionService(
            RawMaterialStockTransactionRepository repository) {
        this.repository = repository;
    }

    public RawMaterialStockTransaction saveTransaction(
            RawMaterialStockTransaction transaction) {
        return repository.save(transaction);
    }

    public List<RawMaterialStockTransaction> getAllTransactions() {
        return repository.findAll();
    }

    public BigDecimal getAvailableStock(Long rawMaterialId) {

        List<RawMaterialStockTransaction> transactions =
                repository.findByRawMaterialId(rawMaterialId);

        BigDecimal stock = BigDecimal.ZERO;

        for (RawMaterialStockTransaction transaction : transactions) {

            if (transaction.getTransactionType().equals("PURCHASE")
                    || transaction.getTransactionType().equals("ADJUSTMENT_IN")) {

                stock = stock.add(transaction.getQuantity());

            } else if (transaction.getTransactionType().equals("ISSUE")
                    || transaction.getTransactionType().equals("ADJUSTMENT_OUT")) {

                stock = stock.subtract(transaction.getQuantity());
            }
        }

        return stock;
    }
}
