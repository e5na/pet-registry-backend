package com.petreg.prototype.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record UserUpdateDto(

    @Pattern(regexp = "^\\d+$", message = "must contain digits only")
    String personalCode,

    @Pattern(regexp = "(?U).*\\S.*", message = "must contain at least one non-whitespace character")
    String firstName,

    @Pattern(regexp = "(?U).*\\S.*", message = "must contain at least one non-whitespace character")
    String lastName,

    @Email
    String email,

    @Pattern(
        regexp = "^\\+[1-9]\\d{6,14}$",
        message = "must be in international format, e.g. +37255512345"
    )
    String phoneNumber,

    @Valid
    OwnerProfileUpdateDto ownerProfile,

    @Valid
    VetProfileUpdateDto vetProfile
) {}
