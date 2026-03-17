package com.petreg.prototype.dto;

public record MicrochipResponseDto(
        Long id,
        String microchipNumber,
        String supplier,
        boolean inUse) {
}
