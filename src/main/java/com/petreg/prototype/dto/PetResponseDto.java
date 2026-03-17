package com.petreg.prototype.dto;

import java.time.LocalDate;
import java.util.List;

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
        List<PictureResponseDto> pictures

) {
}
