package com.petreg.prototype.dto;

import com.petreg.prototype.model.type.RoleEnum;

public record LoginRequestDto(
    String personalCode,
    String password,
    RoleEnum roleType
) {}
