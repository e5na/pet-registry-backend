package com.petreg.prototype.dto;

import com.petreg.prototype.model.type.RoleEnum;

import jakarta.validation.constraints.Pattern;

public record RoleUpdateDto(
    @Pattern(regexp = "(?U).*\\S.*", message = "must contain at least one non-whitespace character")
    RoleEnum name
) {}
