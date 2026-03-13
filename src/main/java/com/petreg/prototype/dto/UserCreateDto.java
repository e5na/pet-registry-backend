package com.petreg.prototype.dto;

import jakarta.validation.constraints.AssertTrue;

public record UserCreateDto(
    String personalCode,
    String firstName,
    String lastName,
    String email,
    String phoneNumber,
    String password,
    OwnerProfileCreateDto ownerProfile
) {
    @AssertTrue(message = "At least one profile must be selected")
    public boolean hasAtLeastOneProfile() {
        // Once more profiles get added, extend this with an OR check
        return ownerProfile != null;
    }
}
