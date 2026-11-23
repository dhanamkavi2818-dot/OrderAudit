
package com.foodfactory.orderaudit;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BOMService {

    private final BOMRepository bomRepository;
    private final BOMItemRepository bomItemRepository;

    public BOMService(BOMRepository bomRepository,
                      BOMItemRepository bomItemRepository) {
        this.bomRepository = bomRepository;
        this.bomItemRepository = bomItemRepository;
    }

    public BOM saveBOM(BOM bom) {
        return bomRepository.save(bom);
    }

    public List<BOM> getAllBOMs() {
        return bomRepository.findAll();
    }

    public BOM getBOMById(Long id) {
        return bomRepository.findById(id).orElse(null);
    }

    public BOMItem saveBOMItem(BOMItem item) {
        return bomItemRepository.save(item);
    }

    public List<BOMItem> getBOMItems(Long bomId) {
        return bomItemRepository.findAll().stream()
                .filter(item -> item.getBom().getId().equals(bomId))
                .toList();
    }

    public void deleteBOM(Long id) {
        bomRepository.deleteById(id);
    }
}
