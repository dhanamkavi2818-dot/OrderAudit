
package com.foodfactory.orderaudit;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductionMaterialUsageRepository
        extends JpaRepository<ProductionMaterialUsage, Long> {

    List<ProductionMaterialUsage> findByProductionRunId(Long productionRunId);
}
