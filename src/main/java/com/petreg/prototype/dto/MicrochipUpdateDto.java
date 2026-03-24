package com.petreg.prototype.dto;

import jakarta.validation.constraints.Pattern;

public record MicrochipUpdateDto(
        @Pattern(regexp = "^\\d{15}$", message = "must contain exactly 15 digits")
        String microchipNumber,

        String importer,
        Boolean inUse) {
}
