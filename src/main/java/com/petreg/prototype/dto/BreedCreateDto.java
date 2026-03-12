package com.petreg.prototype.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BreedCreateDto(

    @NotBlank(message = "Breed must have a name")
    String name,

    @NotNull(message = "Breed must belong to a species")
    Long speciesId

) {}
