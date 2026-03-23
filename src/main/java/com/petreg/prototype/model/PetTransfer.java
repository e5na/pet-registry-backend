package com.petreg.prototype.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.petreg.prototype.model.type.TransferStatus;

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
@Table(name = "ownership_transfers")
public class PetTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //pet_id BIGINT NOT NULL REFERENCES pets(id),
    @ManyToOne(optional = false)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    //old_owner_id BIGINT NOT NULL REFERENCES users(id),
    @ManyToOne(optional = false)
    @JoinColumn(name = "old_owner_id", nullable = false)
    private User oldOwner; // current owner

    //new_owner_id BIGINT NOT NULL REFERENCES users(id),
    @ManyToOne(optional = false)
    @JoinColumn(name = "new_owner_id", nullable = false)
    private User newOwner; // must have OWNER role (OwnerProfile)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransferStatus status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime startedAt;

    private LocalDateTime confirmedAt;

    public PetTransfer() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public User getOldOwner() {
        return oldOwner;
    }

    public void setOldOwner(User oldOwner) {
        this.oldOwner = oldOwner;
    }

    public User getNewOwner() {
        return newOwner;
    }

    public void setNewOwner(User newOwner) {
        this.newOwner = newOwner;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public void setStatus(TransferStatus status) {
        this.status = status;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(LocalDateTime confirmedAt) {
        this.confirmedAt = confirmedAt;
    }
}
