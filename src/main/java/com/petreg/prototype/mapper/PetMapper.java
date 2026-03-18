package com.petreg.prototype.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.petreg.prototype.dto.PetCreateDto;
import com.petreg.prototype.dto.PetResponseDto;
import com.petreg.prototype.dto.PetUpdateDto;
import com.petreg.prototype.dto.PictureResponseDto;
import com.petreg.prototype.model.Breed;
import com.petreg.prototype.model.Microchip;
import com.petreg.prototype.model.Pet;
import com.petreg.prototype.model.Picture;
import com.petreg.prototype.model.User;

@Component
public class PetMapper {

    private final BreedMapper breedMapper;
    private final MicrochipMapper microchipMapper;
    private final UserMapper userMapper;

    public PetMapper(
            BreedMapper breedMapper,
            MicrochipMapper microchipMapper,
            UserMapper userMapper) {
        this.breedMapper = breedMapper;
        this.microchipMapper = microchipMapper;
        this.userMapper = userMapper;
    }

    public PetResponseDto toDto(Pet pet) {
        List<PictureResponseDto> pictureDtos = pet.getPictures() != null
                ? pet.getPictures().stream()
                        .map(p -> new PictureResponseDto(p.getId(), p.getFileName(), p.getPicture()))
                        .collect(Collectors.toList())
                : Collections.emptyList();

        return new PetResponseDto(
                pet.getId(),
                pet.getName(),
                pet.getSex(),
                pet.getBirthDate(),
                pet.getColor(),
                pet.getStatus(),
                breedMapper.toDto(pet.getBreed()),
                pet.getMicrochip() != null ? microchipMapper.toDto(pet.getMicrochip()) : null,
                pet.getOwner() != null ? userMapper.toDto(pet.getOwner()) : null,
                pictureDtos);
    }

    public Pet fromDto(
            PetCreateDto dto,
            Breed breed,
            Microchip microchip,
            User owner) {

        return new Pet(
                dto.name(),
                dto.sex(),
                dto.birthDate(),
                dto.color(),
                dto.status(),
                breed,
                microchip,
                owner);
    }

    public void update(
            PetUpdateDto dto,
            Pet pet,
            Breed breed,
            Microchip microchip,
            User owner) {

        if (dto.name() != null) {
            pet.setName(dto.name());
        }

        if (dto.sex() != null) {
            pet.setSex(dto.sex());
        }

        if (dto.birthDate() != null) {
            pet.setBirthDate(dto.birthDate());
        }

        if (dto.color() != null) {
            pet.setColor(dto.color());
        }

        if (dto.breedId() != null) {
            pet.setBreed(breed);
        }

        if (microchip != null) {
            pet.setMicrochip(microchip);
        }

        if (owner != null) {
            pet.setOwner(owner);
        }

        if (dto.picture() != null) {
            Picture picture = new Picture();
            picture.setPicture(dto.picture());
            picture.setPet(pet);
            picture.setFileName("uploaded_picture"); // Default name
            pet.getPictures().add(picture);
        }
    }
}
