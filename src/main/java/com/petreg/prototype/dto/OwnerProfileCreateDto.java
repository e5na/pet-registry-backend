package com.petreg.prototype.dto;

import jakarta.validation.constraints.NotBlank;

public record OwnerProfileCreateDto(
    @NotBlank
    String address
) {}
