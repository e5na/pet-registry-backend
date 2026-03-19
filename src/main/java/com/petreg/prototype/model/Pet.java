package com.petreg.prototype.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.petreg.prototype.model.type.PetStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private String name;
    private char sex;
    private LocalDate birthDate;
    private String color;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetStatus status;

    @ManyToOne
    @JoinColumn(name = "breed_id")
    private Breed breed;

    @OneToOne
    @JoinColumn(name = "microchip_id", referencedColumnName = "id", unique = true)
    private Microchip microchip;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Picture> pictures = new ArrayList<>();

    public Pet() {
    }

    public Pet(
            String name,
            char sex,
            LocalDate birthDate,
            String color,
            PetStatus status,
            Breed breed,
            Microchip microchip,
            User owner) {
        this.name = name;
        this.sex = sex;
        this.birthDate = birthDate;
        this.color = color;
        this.status = status;
        this.breed = breed;
        this.microchip = microchip;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public PetStatus getStatus() {
        return status;
    }

    public void setStatus(PetStatus status) {
        this.status = status;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    public Breed getBreed() {
        return breed;
    }

    public void setBreed(Breed breed) {
        this.breed = breed;
    }

    public Microchip getMicrochip() {
        return microchip;
    }

    public void setMicrochip(Microchip microchip) {
        this.microchip = microchip;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
