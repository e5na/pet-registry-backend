package com.petreg.prototype.mapper;

import org.springframework.stereotype.Component;

import com.petreg.prototype.dto.MicrochipCreateDto;
import com.petreg.prototype.dto.MicrochipResponseDto;
import com.petreg.prototype.dto.MicrochipUpdateDto;
import com.petreg.prototype.model.Microchip;

@Component
public class MicrochipMapper {

    public MicrochipResponseDto toDto(Microchip microchip) {
        return new MicrochipResponseDto(
                microchip.getId(),
                microchip.getMicrochipNumber(),
                microchip.getImporter(),
                microchip.getInUse());
    }

    public Microchip fromDto(MicrochipCreateDto dto) {
        return new Microchip(
                dto.microchipNumber(),
                dto.supplier(),
                false);
    }

    public void update(MicrochipUpdateDto dto, Microchip microchip) {
        // --- Only modify non-null data fields ---

        if (dto.microchipNumber() != null) {
            microchip.setMicrochipNumber(dto.microchipNumber());
        }

        if (dto.supplier() != null) {
            microchip.setImporter(dto.supplier());
        }

        if (dto.inUse() != null) {
            microchip.setInUse(dto.inUse());
        }
    }
}
