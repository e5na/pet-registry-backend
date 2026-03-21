package com.petreg.prototype.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.petreg.prototype.dto.PetEventResponseDto;
import com.petreg.prototype.exception.BadRequestException;
import com.petreg.prototype.mapper.PetEventMapper;
import com.petreg.prototype.model.Pet;
import com.petreg.prototype.model.PetEvent;
import com.petreg.prototype.model.Role;
import com.petreg.prototype.model.User;
import com.petreg.prototype.model.type.PetLifeCycleEvent;
import com.petreg.prototype.model.type.RoleEnum;
import com.petreg.prototype.repository.PetEventRepository;

@Service
public class PetEventService {

    private final PetEventRepository petEventRepository;
    private final PetEventMapper petEventMapper;

    public PetEventService(
        PetEventRepository petEventRepository,
        PetEventMapper petEventMapper
    ) {
        this.petEventRepository = petEventRepository;
        this.petEventMapper = petEventMapper;
    }

    public PetEventResponseDto logEvent(
        Pet pet,
        User user,
        Role userRole,
        PetLifeCycleEvent eventType,
        String description
    ) {
        validateRoleForEvent(userRole, eventType);

        PetEvent event = new PetEvent(
            pet,
            user,
            userRole,
            eventType,
            LocalDateTime.now(),
            resolveDescription(eventType, description)
        );

        return petEventMapper.toDto(petEventRepository.save(event));
    }

    public List<PetEventResponseDto> getPetHistory(Long petId) {
        return petEventRepository.findByPet_IdOrderByTimestampDesc(petId)
            .stream()
            .map(petEventMapper::toDto)
            .toList();
    }

    private void validateRoleForEvent(Role role, PetLifeCycleEvent eventType) {
        RoleEnum roleName = role.getName();
        switch (eventType) {
            case REGISTRATION -> {
                if (roleName != RoleEnum.ADMIN && roleName != RoleEnum.VET) {
                    throw new BadRequestException("Only an admin or veterinarian can register a pet");
                }
            }
            case OWNER_CHANGE_STARTED -> {
                if (roleName != RoleEnum.OWNER) {
                    throw new BadRequestException("Only an owner can start an owner change");
                }
            }
            case OWNER_CHANGE_CONFIRMED -> {
                if (roleName != RoleEnum.OWNER) {
                    throw new BadRequestException("Only an owner can confirm an owner change");
                }
            }
            case LOST_REPORTED -> {
                if (roleName != RoleEnum.OWNER) {
                    throw new BadRequestException("Only an owner can report a pet as lost");
                }
            }
            // This should be a shelter worker job, but without shelter tables, I will put VET here for now
            case FOUND_IN_SHELTER -> {
                if (roleName != RoleEnum.VET && roleName != RoleEnum.ADMIN) {
                    throw new BadRequestException("Only a shelter worker or admin can mark a pet as found");
                }
            }
            case MEDICAL_CHECKUP, TREATMENT_RECORDED -> {
                if (roleName != RoleEnum.VET) {
                    throw new BadRequestException("Only a veterinarian can record this event");
                }
            }
            case PERMANENT_EXPORT -> {
                if (roleName != RoleEnum.OWNER && roleName != RoleEnum.ADMIN) {
                    throw new BadRequestException("Only an owner or admin can record permanent export");
                }
            }
            case DEATH_REPORTED -> {
                if (roleName != RoleEnum.OWNER && roleName != RoleEnum.VET && roleName != RoleEnum.ADMIN) {
                    throw new BadRequestException("This role cannot report a pet death");
    }
}
        }
    }

    private String resolveDescription(PetLifeCycleEvent eventType, String description) {
        if (description != null && !description.isBlank()) {
            return description;
        }

        return switch (eventType) {
            case REGISTRATION -> "Pet registered";
            case OWNER_CHANGE_STARTED -> "Owner change started";
            case OWNER_CHANGE_CONFIRMED -> "Owner change confirmed";
            case LOST_REPORTED -> "Pet reported lost";
            case FOUND_IN_SHELTER -> "Pet found in shelter";
            case MEDICAL_CHECKUP -> "Medical checkup recorded";
            case TREATMENT_RECORDED -> "Treatment recorded";
            case DEATH_REPORTED -> "Death reported";
            case PERMANENT_EXPORT -> "Permanent export recorded";
        };
    }
}
