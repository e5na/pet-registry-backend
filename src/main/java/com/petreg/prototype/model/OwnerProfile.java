package com.petreg.prototype.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "owner_profiles")
public class OwnerProfile {

    // Will be shared with the user's id
    @Id
    private Long id;

    private String address;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    // --- Constructors ---

    public OwnerProfile() {}

    public OwnerProfile(String address) {
        this.address = address;
    }

    // --- Getters and setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
