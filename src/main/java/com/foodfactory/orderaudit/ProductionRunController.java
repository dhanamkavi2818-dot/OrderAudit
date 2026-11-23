
package com.foodfactory.orderaudit;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/production")
public class ProductionRunController {

    private final ProductionRunService productionService;
    private final BOMService bomService;
    private final RawMaterialService rawMaterialService;

    public ProductionRunController(
            ProductionRunService productionService,
            BOMService bomService,
            RawMaterialService rawMaterialService) {

        this.productionService = productionService;
        this.bomService = bomService;
        this.rawMaterialService = rawMaterialService;
    }

    
@GetMapping
public String showProductionPage(Model model) {

    java.util.List<BOM> boms = bomService.getAllBOMs();

    java.util.Map<Long, java.util.List<BOMItem>> itemsByBom =
            new java.util.HashMap<>();

    for (BOM bom : boms) {
        itemsByBom.put(
                bom.getId(),
                bomService.getBOMItems(bom.getId())
        );
    }

    model.addAttribute("boms", boms);
    model.addAttribute("itemsByBom", itemsByBom);
    model.addAttribute("productionRuns",
            productionService.getAllProductionRuns());

    return "production";
}


    @PostMapping("/save")
    public String saveProduction(
            
@RequestParam Long bomId,
@RequestParam String batchReference,
@RequestParam Integer actualOutputPackets,

            @RequestParam Integer damagedPackets,
            @RequestParam BigDecimal labourCost,
            @RequestParam BigDecimal overheadCost,
            @RequestParam Map<String, String> formData,
            RedirectAttributes redirectAttributes) {

        BOM bom = bomService.getBOMById(bomId);

        if (bom == null) {
            redirectAttributes.addFlashAttribute(
                    "error", "Invalid BOM selected");
            return "redirect:/production";
        }

        try {
            if (actualOutputPackets == null || actualOutputPackets <= 0) {
    throw new IllegalArgumentException(
            "Good packets produced must be greater than zero");
}

if (damagedPackets == null || damagedPackets < 0
        || labourCost == null || labourCost.signum() < 0
        || overheadCost == null || overheadCost.signum() < 0) {
    throw new IllegalArgumentException(
            "Production quantities and costs cannot be negative");
}

            ProductionRun run = new ProductionRun();
            run.setBom(bom);
            run.setBatchReference(batchReference);
            run.setActualOutputPackets(actualOutputPackets);
            run.setDamagedPackets(damagedPackets);
            run.setLabourCost(labourCost);
            run.setOverheadCost(overheadCost);

            Map<RawMaterial, BigDecimal> usage =
                    new LinkedHashMap<>();

            for (BOMItem item :
                    bomService.getBOMItems(bomId)) {

                RawMaterial material = item.getRawMaterial();
                String fieldName = "material_" + material.getId();
                String value = formData.get(fieldName);

                if (value == null || value.isBlank()) {
                    throw new IllegalArgumentException(
                            "Missing usage for "
                            + material.getMaterialName());
                }

                usage.put(material, new BigDecimal(value));
            }

            productionService.saveProductionRun(run, usage);

            redirectAttributes.addFlashAttribute(
                    "success", "Production saved successfully!");

        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute(
                    "error", ex.getMessage());
        }

        return "redirect:/production";
    }
}
