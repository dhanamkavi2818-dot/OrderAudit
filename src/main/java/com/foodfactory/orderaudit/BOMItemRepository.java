
package com.foodfactory.orderaudit;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BOMItemRepository extends JpaRepository<BOMItem, Long> {

    List<BOMItem> findByBomId(Long bomId);

}
