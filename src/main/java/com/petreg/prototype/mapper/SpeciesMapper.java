package com.petreg.prototype.mapper;

import org.springframework.stereotype.Component;

import com.petreg.prototype.dto.SpeciesCreateDto;
import com.petreg.prototype.dto.SpeciesResponseDto;
import com.petreg.prototype.dto.SpeciesUpdateDto;
import com.petreg.prototype.model.Species;

@Component
public class SpeciesMapper {

    public SpeciesResponseDto toDto(Species species) {
        return new SpeciesResponseDto(
            species.getId(),
            species.getName()
        );
    }

    public Species fromDto(SpeciesCreateDto dto) {
        return new Species(
            dto.name()
        );
    }

    public void update(SpeciesUpdateDto dto, Species species) {
        if (dto.name() != null) {
            species.setName(dto.name());
        }
    }
}
