package com.petreg.prototype.dto;

import jakarta.validation.constraints.NotBlank;

public record SpeciesCreateDto (

    @NotBlank(message = "Species must have a name")
    String name

) {}
