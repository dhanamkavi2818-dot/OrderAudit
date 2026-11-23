
package com.foodfactory.orderaudit;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RawMaterialService {

    private final RawMaterialRepository rawMaterialRepository;

    public RawMaterialService(RawMaterialRepository rawMaterialRepository) {
        this.rawMaterialRepository = rawMaterialRepository;
    }

    public RawMaterial saveRawMaterial(RawMaterial rawMaterial) {
        return rawMaterialRepository.save(rawMaterial);
    }

    public List<RawMaterial> getAllRawMaterials() {
        return rawMaterialRepository.findAll();
    }

    public RawMaterial getRawMaterialById(Long id) {
        return rawMaterialRepository.findById(id).orElse(null);
    }

    public void deleteRawMaterial(Long id) {
        rawMaterialRepository.deleteById(id);
    }
}
