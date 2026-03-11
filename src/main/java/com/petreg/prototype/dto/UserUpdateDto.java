package com.petreg.prototype.dto;

public record UserUpdateDto(
    String personalCode,
    String firstName,
    String lastName,
    String email,
    String phoneNumber,
    String address
) {}
