package com.petreg.prototype.dto;

import jakarta.validation.constraints.Pattern;

public record SpeciesUpdateDto(
        @Pattern(regexp = "^\\d+$", message = "must contain digits only")
        String name) {}
