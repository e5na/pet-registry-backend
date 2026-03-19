package com.petreg.prototype.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record BreedUpdateDto(
        @Pattern(regexp = "^\\d+$", message = "must contain digits only")
        String name,

        @Positive
        Long speciesId) {
}
