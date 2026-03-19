package com.petreg.prototype.dto;

import com.petreg.prototype.model.type.RoleEnum;

import jakarta.validation.constraints.NotBlank;

public record RoleCreateDto(
    @NotBlank
    RoleEnum name
) {}
