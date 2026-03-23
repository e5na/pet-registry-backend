package com.petreg.prototype.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PetCreateDto(

        @NotBlank String name,
        @NotNull Character sex,
        @NotNull LocalDate birthDate,
        @NotBlank String color,
        @NotNull Long breedId,

        Long microchipId,
        Long ownerId

) {
}
