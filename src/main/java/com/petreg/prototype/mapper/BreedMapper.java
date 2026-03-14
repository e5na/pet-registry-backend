package com.petreg.prototype.mapper;

import org.springframework.stereotype.Component;

import com.petreg.prototype.dto.BreedCreateDto;
import com.petreg.prototype.dto.BreedResponseDto;
import com.petreg.prototype.dto.BreedUpdateDto;
import com.petreg.prototype.model.Breed;
import com.petreg.prototype.model.Species;

@Component
public class BreedMapper {

    private final SpeciesMapper speciesMapper;

    public BreedMapper(SpeciesMapper speciesMapper) {
        this.speciesMapper = speciesMapper;
    }

    public BreedResponseDto toDto(Breed breed) {
        return new BreedResponseDto(
            breed.getId(),
            breed.getName(),
            speciesMapper.toDto(breed.getSpecies())
        );
    }

    public Breed fromDto(BreedCreateDto dto, Species species) {
        return new Breed(
            dto.name(),
            species
        );
    }

    public void update(BreedUpdateDto dto, Breed breed, Species species) {

        if (dto.name() != null) {
            breed.setName(dto.name());
        }

        if (dto.speciesId() != null) {
            breed.setSpecies(species);
        }
    }
}
