package com.petreg.prototype.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record PetUpdateDto(

    @Pattern(regexp = "(?U).*\\S.*", message = "must contain at least one non-whitespace character")
    String name,

    @Pattern(regexp = "F|M", message = "must contain F or M")
    Character sex,

    @Past
    LocalDate birthDate,

    @Pattern(regexp = "(?U).*\\S.*", message = "must contain at least one non-whitespace character")
    String color,

    @Positive
    Long breedId,

    @Positive
    Long microchipId,

    @Positive
    Long ownerId,

    byte[] picture

) {}
