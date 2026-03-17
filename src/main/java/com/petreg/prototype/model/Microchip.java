package com.petreg.prototype.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "microchips")
public class Microchip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String chipNumber;

    private String supplier;
    private boolean inUse;

    // --- Constructors ---

    public Microchip() {}

    public Microchip(String chipNumber, String supplier, boolean inUse) {
        this.chipNumber = chipNumber;
        this.supplier = supplier;
        this.inUse = inUse;
    }

    // --- Getters and setters ---

    public Long getId() {
        return id;
    }

    public String getChipNumber() {
        return chipNumber;
    }

    public void setChipNumber(String chipNumber) {
        this.chipNumber = chipNumber;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public boolean getInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }
}
