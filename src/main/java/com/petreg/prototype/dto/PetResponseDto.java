package com.petreg.prototype.dto;

import java.time.LocalDate;

public record PetResponseDto(

    Long id,
    String name,
    char sex,
    LocalDate birthDate,
    String color,
    SpeciesResponseDto species,
    BreedResponseDto breed,
    MicrochipResponseDto microchip,
    UserResponseDto owner,

    byte[] picture

) {}
