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
            microchip.getChipNumber(),
            microchip.getSupplier(),
            microchip.getInUse()
        );
    }

    public Microchip fromDto(MicrochipCreateDto dto) {
        return new Microchip(
            dto.chipNumber(),
            dto.supplier(),
            // Mark as available by default
            false
        );
    }

    public void update(MicrochipUpdateDto dto, Microchip microchip) {
        // --- Only modify non-null data fields ---

        if (dto.chipNumber() != null) {
            microchip.setChipNumber(dto.chipNumber());
        }

        if (dto.supplier() != null) {
            microchip.setSupplier(dto.supplier());
        }

        if (dto.inUse() != null) {
            microchip.setInUse(dto.inUse());
        }
    }
}
