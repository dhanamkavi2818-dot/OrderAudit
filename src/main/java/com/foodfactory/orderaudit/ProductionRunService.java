
package com.foodfactory.orderaudit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ProductionRunService {

    private final ProductionRunRepository productionRunRepository;
    private final ProductionMaterialUsageRepository usageRepository;
    private final RawMaterialStockTransactionRepository stockRepository;
    private final RawMaterialStockTransactionService stockService;

private final FinishedGoodsStockTransactionService finishedGoodsService;

    

    public ProductionRunService(
            ProductionRunRepository productionRunRepository,
            ProductionMaterialUsageRepository usageRepository,
            RawMaterialStockTransactionRepository stockRepository,
            RawMaterialStockTransactionService stockService,
FinishedGoodsStockTransactionService finishedGoodsService) {

        this.productionRunRepository = productionRunRepository;
        this.usageRepository = usageRepository;
        this.stockRepository = stockRepository;
        this.stockService = stockService;
        this.finishedGoodsService = finishedGoodsService;
    }

    @Transactional
    public ProductionRun saveProductionRun(
            ProductionRun productionRun,
            Map<RawMaterial, BigDecimal> materialUsage) {
                String batchReference = productionRun.getBatchReference();

if (batchReference == null || batchReference.isBlank()) {
    throw new IllegalArgumentException(
            "Batch reference is required");
}

batchReference = batchReference.trim();
productionRun.setBatchReference(batchReference);

if (productionRunRepository.existsByBatchReference(batchReference)) {
    throw new IllegalArgumentException(
            "This production batch has already been recorded");
}

        if (materialUsage == null || materialUsage.isEmpty()) {
            throw new IllegalArgumentException(
                    "Production must include raw material usage");
        }

        for (Map.Entry<RawMaterial, BigDecimal> entry :
                materialUsage.entrySet()) {

            RawMaterial material = entry.getKey();
            BigDecimal quantity = entry.getValue();

            if (quantity == null ||
                    quantity.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException(
                        "Material quantity must be positive");
            }

            BigDecimal available =
                    stockService.getAvailableStock(material.getId());

            if (available.compareTo(quantity) < 0) {
                throw new IllegalArgumentException(
                        "Insufficient stock for " +
                        material.getMaterialName());
            }
        }

        productionRun.setProductionDate(LocalDateTime.now());

        ProductionRun savedRun =
                productionRunRepository.save(productionRun);

        for (Map.Entry<RawMaterial, BigDecimal> entry :
                materialUsage.entrySet()) {

            RawMaterial material = entry.getKey();
            BigDecimal quantity = entry.getValue();

            ProductionMaterialUsage usage =
                    new ProductionMaterialUsage();

            usage.setProductionRun(savedRun);
            usage.setRawMaterial(material);
            usage.setActualQuantity(quantity);

            usageRepository.save(usage);

            RawMaterialStockTransaction transaction =
                    new RawMaterialStockTransaction();

            transaction.setRawMaterial(material);
            transaction.setTransactionType("ISSUE");
            transaction.setQuantity(quantity);
            transaction.setReference(
                    "PRODUCTION-" + savedRun.getId());
            transaction.setTransactionDate(LocalDateTime.now());

            stockRepository.save(transaction);
        }

FinishedGoodsStockTransaction finishedTransaction =
        new FinishedGoodsStockTransaction();

finishedTransaction.setProduct(savedRun.getBom().getProduct());
finishedTransaction.setTransactionType("PRODUCTION");
finishedTransaction.setQuantity(savedRun.getActualOutputPackets());
finishedTransaction.setReference(
        "PRODUCTION-" + savedRun.getId());
finishedTransaction.setTransactionDate(LocalDateTime.now());

finishedGoodsService.saveTransaction(finishedTransaction);


        return savedRun;
    }

    public List<ProductionRun> getAllProductionRuns() {
        return productionRunRepository.findAll();
    }

    public ProductionRun getProductionRunById(Long id) {
        return productionRunRepository.findById(id).orElse(null);
    }
}
