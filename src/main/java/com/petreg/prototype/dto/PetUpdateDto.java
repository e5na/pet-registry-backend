package com.petreg.prototype.dto;

import java.time.LocalDate;

public record PetUpdateDto(

    String name,
    Character sex,
    LocalDate birthDate,
    String color,
    Long speciesId,
    Long breedId,
    Long microchipId,
    Long ownerId,

    byte[] picture

) {}
