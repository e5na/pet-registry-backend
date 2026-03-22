package com.petreg.prototype.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.petreg.prototype.model.type.PetLifeCycleEvent;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="pet_lifecycle_events")
public class PetEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_role_id")
    private Role userRole;

    @Enumerated(EnumType.STRING)
    private PetLifeCycleEvent eventType;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime timestamp;

    private String description;

    public PetEvent() {}

    public PetEvent(
        Pet pet,
        User user,
        Role userRole,
        PetLifeCycleEvent eventType,
        String description
    ) {
        this.pet = pet;
        this.user = user;
        this.userRole = userRole;
        this.eventType = eventType;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public Pet getPet() {
        return pet;
    }

    public User getUser() {
        return user;
    }

    public Role getUserRole() {
        return userRole;
    }

    public PetLifeCycleEvent getEventType() {
        return eventType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUserRole(Role userRole) {
        this.userRole = userRole;
    }

    public void setEventType(PetLifeCycleEvent eventType) {
        this.eventType = eventType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
