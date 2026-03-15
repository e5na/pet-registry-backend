package com.petreg.prototype.dto;

import java.util.Set;

public record UserResponseDto(
    Long id,
    String personalCode,
    String firstName,
    String lastName,
    String email,
    String phoneNumber,
    Set<RoleResponseDto> roles,
    // optional profiles
    OwnerProfileResponseDto ownerProfile
) {}
