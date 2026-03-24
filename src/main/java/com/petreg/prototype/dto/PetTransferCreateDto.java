package com.petreg.prototype.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PetTransferCreateDto(
        @NotNull
        @Positive
        Long newOwnerId
) {
}
