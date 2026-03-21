package com.petreg.prototype.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "veterinary_profiles")
public class VetProfile {

    // Will be shared with the user's id
    @Id
    private Long id;

    String licenseNumber;

    String specialization;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    // --- Constructors ---

    public VetProfile() {}

    public VetProfile(String licenceNumber, String specialization) {
        this.licenseNumber = licenceNumber;
        this.specialization = specialization;
    }

    // --- Getters and setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenceNumber) {
        this.licenseNumber = licenceNumber;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
