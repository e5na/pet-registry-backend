package com.petreg.prototype.dto;

import java.time.LocalDate;
import java.util.List;

import com.petreg.prototype.model.type.PetStatus;

public record PetResponseDto(

        Long id,
        String name,
        char sex,
        LocalDate birthDate,
        String color,
        PetStatus status,
        BreedResponseDto breed,
        MicrochipResponseDto microchip,
        UserResponseDto owner,
        List<PictureResponseDto> pictures

) {
}
