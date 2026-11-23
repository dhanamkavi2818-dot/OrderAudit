
package com.foodfactory.orderaudit;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProductionVarianceController {

    private final ProductionRunService productionRunService;
    private final BOMItemRepository bomItemRepository;
    private final ProductionMaterialUsageRepository usageRepository;

    public ProductionVarianceController(
            ProductionRunService productionRunService,
            BOMItemRepository bomItemRepository,
            ProductionMaterialUsageRepository usageRepository) {

        this.productionRunService = productionRunService;
        this.bomItemRepository = bomItemRepository;
        this.usageRepository = usageRepository;
    }

    @GetMapping("/production-variance")
    public String showProductionVariance(Model model) {

        List<Map<String, Object>> reports = new ArrayList<>();

        for (ProductionRun run :
                productionRunService.getAllProductionRuns()) {

            Map<String, Object> report = new HashMap<>();

            int planned = run.getBom().getBatchOutputPackets();
            int actual = run.getActualOutputPackets();

            BigDecimal yieldVariance = calculateVariance(
                    BigDecimal.valueOf(planned),
                    BigDecimal.valueOf(actual)
            );

            report.put("batchReference", run.getBatchReference());
            report.put("productName",
                    run.getBom().getProduct().getProductName());
            report.put("planned", planned);
            report.put("actual", actual);
            report.put("damaged", run.getDamagedPackets());
            report.put("yieldVariance", yieldVariance);
            report.put("yieldAlert",
                    yieldVariance.abs().compareTo(
                            new BigDecimal("3")) > 0);

            List<Map<String, Object>> materials = new ArrayList<>();

            List<BOMItem> bomItems =
                    bomItemRepository.findByBomId(run.getBom().getId());

            List<ProductionMaterialUsage> usages =
                    usageRepository.findByProductionRunId(run.getId());

            for (BOMItem item : bomItems) {

                BigDecimal plannedQty = item.getQuantityRequired();
                BigDecimal actualQty = BigDecimal.ZERO;

                for (ProductionMaterialUsage usage : usages) {

                    if (usage.getRawMaterial().getId().equals(
                            item.getRawMaterial().getId())) {

                        actualQty = actualQty.add(
                                usage.getActualQuantity());
                    }
                }

                BigDecimal variance =
                        calculateVariance(plannedQty, actualQty);

                Map<String, Object> material = new HashMap<>();

                material.put("name",
                        item.getRawMaterial().getMaterialName());
                material.put("unit",
                        item.getRawMaterial().getUnit());
                material.put("planned", plannedQty);
                material.put("actual", actualQty);
                material.put("variance", variance);
                material.put("alert",
                        variance.abs().compareTo(
                                new BigDecimal("3")) > 0);

                materials.add(material);
            }

            report.put("materials", materials);
            reports.add(report);
        }

        model.addAttribute("reports", reports);

        return "production-variance";
    }

    private BigDecimal calculateVariance(
            BigDecimal planned, BigDecimal actual) {

        if (planned == null ||
                planned.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return actual.subtract(planned)
                .multiply(new BigDecimal("100"))
                .divide(planned, 2, RoundingMode.HALF_UP);
    }
}
