package com.petreg.prototype.dto;

public record UserCreateDto(
    String personalCode,
    String password,
    String firstName,
    String lastName,
    String email,
    String phoneNumber,
    String address
) {}
