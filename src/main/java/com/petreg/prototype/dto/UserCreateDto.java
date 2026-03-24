package com.petreg.prototype.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCreateDto(
    @NotNull
    @Pattern(regexp = "^\\d+$", message = "must contain digits only")
    String personalCode,

    @NotBlank
    String firstName,

    @NotBlank
    String lastName,

    @NotNull
    @Email
    String email,

    @Pattern(
        regexp = "^\\+[1-9]\\d{6,14}$",
        message = "must be in international format, e.g. +37255512345"
    )
    String phoneNumber,

    @NotBlank
    @Size(min = 4, max = 32)
    String password,

    @Valid
    OwnerProfileCreateDto ownerProfile,

    @Valid
    VetProfileCreateDto vetProfile
) {
    @AssertTrue(message = "At least one profile must be selected")
    public boolean hasAtLeastOneProfile() {
        // Once more profiles get added, extend this with an OR check
        return ownerProfile != null || vetProfile != null;
    }
}
