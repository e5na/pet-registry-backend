package com.petreg.prototype.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petreg.prototype.model.PetTransfer;
import com.petreg.prototype.model.type.TransferStatus;

public interface PetTransferRepository extends JpaRepository<PetTransfer, Long> {

    boolean existsByPetIdAndStatus(Long petId, TransferStatus status);

    Optional<PetTransfer> findByIdAndStatus(Long id, TransferStatus status);
}
