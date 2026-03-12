package com.petreg.prototype.dto;

public record UserCreateDto(
    String personalCode,
    String firstName,
    String lastName,
    String email,
    String phoneNumber,
    String address
) {}
