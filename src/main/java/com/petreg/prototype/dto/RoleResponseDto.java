package com.petreg.prototype.dto;

import com.petreg.prototype.model.type.RoleEnum;

public record RoleResponseDto(
    Long id,
    RoleEnum name
) {}
