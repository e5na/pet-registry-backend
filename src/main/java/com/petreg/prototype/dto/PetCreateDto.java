package com.petreg.prototype.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.petreg.prototype.model.type.PetStatus;

public record PetCreateDto(

        @NotBlank String name,
        @NotNull Character sex,
        @NotNull LocalDate birthDate,
        @NotBlank String color,
        @NotNull PetStatus status,
        @NotNull Long breedId,

        Long microchipId,
        Long ownerId

) {
}
