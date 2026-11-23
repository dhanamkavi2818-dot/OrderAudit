
package com.foodfactory.orderaudit;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/boms")
public class BOMController {

    private final BOMService bomService;
    private final ProductService productService;
    private final RawMaterialService rawMaterialService;

    public BOMController(BOMService bomService,
                         ProductService productService,
                         RawMaterialService rawMaterialService) {
        this.bomService = bomService;
        this.productService = productService;
        this.rawMaterialService = rawMaterialService;
    }

    @GetMapping
public String showBOMs(Model model) {

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
    model.addAttribute("products", productService.getAllProducts());
    model.addAttribute("rawMaterials", rawMaterialService.getAllRawMaterials());

    return "boms";
}

    @PostMapping("/save")
    public String saveBOM(@RequestParam Long productId,
                          @RequestParam Integer batchOutputPackets) {

        Product product = productService.getProductById(productId);

        if (product != null) {
            BOM bom = new BOM();
            bom.setProduct(product);
            bom.setBatchOutputPackets(batchOutputPackets);
            bomService.saveBOM(bom);
        }

        return "redirect:/boms";
    }

    @PostMapping("/save-item")
    public String saveBOMItem(@RequestParam Long bomId,
                              @RequestParam Long rawMaterialId,
                              @RequestParam java.math.BigDecimal quantityRequired) {

        BOM bom = bomService.getBOMById(bomId);
        RawMaterial material = rawMaterialService.getRawMaterialById(rawMaterialId);

        if (bom != null && material != null) {
            BOMItem item = new BOMItem();
            item.setBom(bom);
            item.setRawMaterial(material);
            item.setQuantityRequired(quantityRequired);
            bomService.saveBOMItem(item);
        }

        return "redirect:/boms";
    }
}
