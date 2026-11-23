
package com.foodfactory.orderaudit;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FinishedGoodsStockTransactionRepository
        extends JpaRepository<FinishedGoodsStockTransaction, Long> {

    List<FinishedGoodsStockTransaction> findByProductId(Long productId);
}
