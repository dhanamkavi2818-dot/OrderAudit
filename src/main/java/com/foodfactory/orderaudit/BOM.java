
package com.foodfactory.orderaudit;

import jakarta.persistence.*;

@Entity
@Table(name = "boms")
public class BOM {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Integer batchOutputPackets;

    public BOM() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getBatchOutputPackets() {
        return batchOutputPackets;
    }

    public void setBatchOutputPackets(Integer batchOutputPackets) {
        this.batchOutputPackets = batchOutputPackets;
    }
}
