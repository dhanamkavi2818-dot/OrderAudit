package com.foodfactory.orderaudit;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RawMaterialStockTransactionRepository
        extends JpaRepository<RawMaterialStockTransaction, Long> {

    List<RawMaterialStockTransaction> findByRawMaterialId(Long rawMaterialId);
}