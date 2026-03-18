package com.petreg.prototype.dto;

import java.time.LocalDate;

import com.petreg.prototype.model.type.PetStatus;

public record PetUpdateDto(

    String name,
    Character sex,
    LocalDate birthDate,
    String color,
    PetStatus status,
    Long breedId,
    Long microchipId,
    Long ownerId,

    byte[] picture

) {}
