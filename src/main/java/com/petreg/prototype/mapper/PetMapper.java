package com.petreg.prototype.mapper;

import org.springframework.stereotype.Component;

import com.petreg.prototype.dto.PetCreateDto;
import com.petreg.prototype.dto.PetResponseDto;
import com.petreg.prototype.dto.PetUpdateDto;
import com.petreg.prototype.model.Breed;
import com.petreg.prototype.model.Microchip;
import com.petreg.prototype.model.Pet;
import com.petreg.prototype.model.Species;
import com.petreg.prototype.model.User;

@Component
public class PetMapper {

    private final SpeciesMapper speciesMapper;
    private final BreedMapper breedMapper;
    private final MicrochipMapper microchipMapper;
    private final UserMapper userMapper;

    public PetMapper(
        SpeciesMapper speciesMapper,
        BreedMapper breedMapper,
        MicrochipMapper microchipMapper,
        UserMapper userMapper
    ) {
        this.speciesMapper = speciesMapper;
        this.breedMapper = breedMapper;
        this.microchipMapper = microchipMapper;
        this.userMapper = userMapper;
    }

    public PetResponseDto toDto(Pet pet) {

        return new PetResponseDto(
            pet.getId(),
            pet.getName(),
            pet.getSex(),
            pet.getBirthDate(),
            pet.getColor(),
            speciesMapper.toDto(pet.getSpecies()),
            breedMapper.toDto(pet.getBreed()),
            pet.getMicrochip() != null ? microchipMapper.toDto(pet.getMicrochip()) : null,
            pet.getOwner() != null ? userMapper.toDto(pet.getOwner()) : null,
            pet.getPicture()
        );
    }

    public Pet fromDto(
        PetCreateDto dto,
        Species species,
        Breed breed,
        Microchip microchip,
        User owner
    ) {

        Pet pet = new Pet(
            dto.name(),
            dto.sex(),
            dto.birthDate(),
            dto.color(),
            species,
            breed,
            microchip,
            owner
        );

        if (dto.picture() != null) {
            pet.setPicture(dto.picture());
        }

        return pet;
    }

    public void update(
        PetUpdateDto dto,
        Pet pet,
        Species species,
        Breed breed,
        Microchip microchip,
        User owner
    ) {

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

        if (dto.picture() != null) {
            pet.setPicture(dto.picture());
        }

        if (dto.speciesId() != null) {
            pet.setSpecies(species);
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
    }
}
