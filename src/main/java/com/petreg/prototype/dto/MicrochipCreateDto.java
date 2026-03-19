package com.petreg.prototype.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MicrochipCreateDto(
        @NotBlank
        @Pattern(regexp = "^\\d{15}$", message = "must contain exactly 15 digits")
        String microchipNumber,

        String importer) {
}
