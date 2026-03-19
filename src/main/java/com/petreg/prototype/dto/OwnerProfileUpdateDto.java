package com.petreg.prototype.dto;

import jakarta.validation.constraints.Pattern;

public record OwnerProfileUpdateDto(
    @Pattern(regexp = "(?U).*\\S.*", message = "must contain at least one non-whitespace character")
    String address
) {}
