

package com.foodfactory.orderaudit;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductionRunRepository
        extends JpaRepository<ProductionRun, Long> {

    boolean existsByBatchReference(String batchReference);
}
