
package com.foodfactory.orderaudit;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/raw-materials")
public class RawMaterialController {

    private final RawMaterialService rawMaterialService;

    public RawMaterialController(RawMaterialService rawMaterialService) {
        this.rawMaterialService = rawMaterialService;
    }

    @GetMapping
    public String showRawMaterials(Model model) {
        model.addAttribute("rawMaterials", rawMaterialService.getAllRawMaterials());
        return "raw-materials";
    }

    @PostMapping("/save")
    public String saveRawMaterial(@ModelAttribute RawMaterial rawMaterial) {
        rawMaterialService.saveRawMaterial(rawMaterial);
        return "redirect:/raw-materials";
    }

    @GetMapping("/delete/{id}")
    public String deleteRawMaterial(@PathVariable Long id) {
        rawMaterialService.deleteRawMaterial(id);
        return "redirect:/raw-materials";
    }
}
