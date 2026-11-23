
package com.foodfactory.orderaudit;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "production_material_usage")
public class ProductionMaterialUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "production_run_id", nullable = false)
    private ProductionRun productionRun;

    @ManyToOne
    @JoinColumn(name = "raw_material_id", nullable = false)
    private RawMaterial rawMaterial;

    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal actualQuantity;

    public ProductionMaterialUsage() {
    }

    public Long getId() {
        return id;
    }

    public ProductionRun getProductionRun() {
        return productionRun;
    }

    public void setProductionRun(ProductionRun productionRun) {
        this.productionRun = productionRun;
    }

    public RawMaterial getRawMaterial() {
        return rawMaterial;
    }

    public void setRawMaterial(RawMaterial rawMaterial) {
        this.rawMaterial = rawMaterial;
    }

    public BigDecimal getActualQuantity() {
        return actualQuantity;
    }

    public void setActualQuantity(BigDecimal actualQuantity) {
        this.actualQuantity = actualQuantity;
    }
}
