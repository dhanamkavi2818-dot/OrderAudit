
package com.foodfactory.orderaudit;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductionMaterialUsageService {

    private final ProductionMaterialUsageRepository repository;

    public ProductionMaterialUsageService(
            ProductionMaterialUsageRepository repository) {
        this.repository = repository;
    }

    public ProductionMaterialUsage saveUsage(
            ProductionMaterialUsage usage) {
        return repository.save(usage);
    }

    public List<ProductionMaterialUsage> getUsageByProductionRun(
            Long productionRunId) {
        return repository.findByProductionRunId(productionRunId);
    }

    public List<ProductionMaterialUsage> getAllUsage() {
        return repository.findAll();
    }
}
