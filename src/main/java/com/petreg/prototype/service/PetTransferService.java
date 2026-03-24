package com.petreg.prototype.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.petreg.prototype.dto.PetTransferCreateDto;
import com.petreg.prototype.dto.PetTransferResponseDto;
import com.petreg.prototype.exception.BadRequestException;
import com.petreg.prototype.exception.ConflictException;
import com.petreg.prototype.exception.ForbiddenException;
import com.petreg.prototype.exception.ResourceNotFoundException;
import com.petreg.prototype.model.Pet;
import com.petreg.prototype.model.PetTransfer;
import com.petreg.prototype.model.User;
import com.petreg.prototype.model.type.PetLifeCycleEvent;
import com.petreg.prototype.model.type.TransferStatus;
import com.petreg.prototype.repository.PetRepository;
import com.petreg.prototype.repository.PetTransferRepository;
import com.petreg.prototype.repository.UserRepository;
import com.petreg.prototype.security.ActiveUserContext;

@Service
@Transactional
public class PetTransferService {

    private final ActiveUserContext activeUserContext;
    private final PetEventService petEventService;
    private final PetRepository petRepository;
    private final PetTransferRepository transferRepository;
    private final UserRepository userRepository;

    public PetTransferService(
            ActiveUserContext activeUserContext,
            PetRepository petRepository,
            PetTransferRepository transferRepository,
            UserRepository userRepository,
            PetEventService petEventService) {
        this.activeUserContext = activeUserContext;
        this.petRepository = petRepository;
        this.transferRepository = transferRepository;
        this.userRepository = userRepository;
        this.petEventService = petEventService;
    }

    public PetTransferResponseDto createTransfer(
            Long petId,
            PetTransferCreateDto data) {

        // Load pet and verify current user is the owner
        Pet pet = petRepository.findById(petId)
            .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));

        // TODO: This should be replaced with an authenticated user instance
        User currentUser = pet.getOwner();

        if (!pet.getOwner().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("You are not the owner of this pet");
        }

        // Prevent self-transfer
        if (data.newOwnerId().equals(currentUser.getId())) {
            throw new BadRequestException("Cannot transfer a pet to yourself");
        }

        // Verify new owner exists and is an Owner
        User newOwner = userRepository.findById(data.newOwnerId())
            .orElseThrow(() -> new ResourceNotFoundException("Target user not found"));

        if (newOwner.getOwnerProfile() == null) {
            throw new ForbiddenException("Target user does not have an owner profile");
        }

        // Ensure no PENDING transfer already exists for this pet
        if (transferRepository.existsByPetIdAndStatus(petId, TransferStatus.PENDING)) {
            throw new ConflictException("A pending transfer request already exists for this pet");
        }

        // Create and persist
        PetTransfer transfer = new PetTransfer();
        transfer.setPet(pet);
        transfer.setOldOwner(currentUser);
        transfer.setNewOwner(newOwner);
        transfer.setStatus(TransferStatus.PENDING);

        return toDto(transferRepository.save(transfer));
    }

    public PetTransferResponseDto resolveTransfer(
            Long petId,
            Long id,
            boolean isAccepted) {
        // Load the PENDING transfer
        PetTransfer transfer = transferRepository.findByIdAndStatus(id, TransferStatus.PENDING)
            .orElseThrow(() -> new ResourceNotFoundException("Pending transfer request not found"));

        // Verify it belongs to the right pet
        if (!transfer.getPet().getId().equals(petId)) {
            throw new ResourceNotFoundException("Transfer request not found for this pet");
        }

        // TODO: This should be replaced with an authenticated user check
        User currentUser = transfer.getNewOwner();

        // Only the designated new owner can resolve
        if (!transfer.getNewOwner().getId().equals(currentUser.getId())) {
            throw new ForbiddenException(
                    "Only the target owner can accept or decline this request");
        }

        Pet pet = transfer.getPet();

        if (isAccepted) {
            // Switch owners
            pet.setOwner(transfer.getNewOwner());

            // pet is managed, so no explicit call to petRepository.save(pet) is needed

            transfer.setStatus(TransferStatus.COMPLETED);
        } else {
            transfer.setStatus(TransferStatus.REJECTED);
        }

        transfer.setConfirmedAt(LocalDateTime.now());

        PetTransfer savedTransfer = transferRepository.save(transfer);

        if (isAccepted) {
            // Add event
            petEventService.logEvent(
                pet,
                activeUserContext.getUser(),
                activeUserContext.getActiveRoleType(),
                PetLifeCycleEvent.OWNER_CHANGED,
                null
            );
        }

        return toDto(savedTransfer);
    }

    public PetTransferResponseDto cancelTransfer(Long petId, Long id) {
        PetTransfer transfer = transferRepository.findByIdAndStatus(id, TransferStatus.PENDING)
            .orElseThrow(() -> new ResourceNotFoundException("Pending transfer request not found"));

        if (!transfer.getPet().getId().equals(petId)) {
            throw new ResourceNotFoundException("Transfer request not found for this pet");
        }

        // TODO: This should be replaced with an authenticated user instance
        User currentUser = transfer.getOldOwner();

        if (!transfer.getOldOwner().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("Only the initiator can cancel this request");
        }

        transfer.setStatus(TransferStatus.CANCELLED);
        transfer.setConfirmedAt(LocalDateTime.now());
        return toDto(transferRepository.save(transfer));
    }

    private PetTransferResponseDto toDto(PetTransfer entity) {
        return new PetTransferResponseDto(
            entity.getId(),
            entity.getPet().getId(),
            entity.getOldOwner().getId(),
            entity.getNewOwner().getId(),
            entity.getStatus(),
            entity.getStartedAt(),
            entity.getConfirmedAt()
        );
    }
}
