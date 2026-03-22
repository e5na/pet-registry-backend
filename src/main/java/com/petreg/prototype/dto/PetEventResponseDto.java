package com.petreg.prototype.dto;

import java.time.LocalDateTime;

import com.petreg.prototype.model.type.PetLifeCycleEvent;

public record PetEventResponseDto(
    Long id,
    PetResponseDto pet,
    UserResponseDto user,
    RoleResponseDto userRole,
    PetLifeCycleEvent eventType,
    LocalDateTime timestamp,
    String description
) {}
