
package com.foodfactory.orderaudit;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "production_runs")
public class ProductionRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bom_id", nullable = false)
    private BOM bom;

    @Column(nullable = false)
    private Integer actualOutputPackets;
    @Column(unique = true, nullable = true)
private String batchReference;

public String getBatchReference() {
    return batchReference;
}

public void setBatchReference(String batchReference) {
    this.batchReference = batchReference;
}

    @Column(nullable = false)
    private Integer damagedPackets;

    @Column(precision = 12, scale = 2)
    private BigDecimal labourCost;

    @Column(precision = 12, scale = 2)
    private BigDecimal overheadCost;

    private LocalDateTime productionDate;

    public ProductionRun() {
    }

    public Long getId() {
        return id;
    }

    public BOM getBom() {
        return bom;
    }

    public void setBom(BOM bom) {
        this.bom = bom;
    }

    public Integer getActualOutputPackets() {
        return actualOutputPackets;
    }

    public void setActualOutputPackets(Integer actualOutputPackets) {
        this.actualOutputPackets = actualOutputPackets;
    }

    public Integer getDamagedPackets() {
        return damagedPackets;
    }

    public void setDamagedPackets(Integer damagedPackets) {
        this.damagedPackets = damagedPackets;
    }

    public BigDecimal getLabourCost() {
        return labourCost;
    }

    public void setLabourCost(BigDecimal labourCost) {
        this.labourCost = labourCost;
    }

    public BigDecimal getOverheadCost() {
        return overheadCost;
    }

    public void setOverheadCost(BigDecimal overheadCost) {
        this.overheadCost = overheadCost;
    }

    public LocalDateTime getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(LocalDateTime productionDate) {
        this.productionDate = productionDate;
    }
}
