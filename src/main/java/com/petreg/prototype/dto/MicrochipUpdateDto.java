package com.petreg.prototype.dto;

public record MicrochipUpdateDto(
        String microchipNumber,
        String importer,
        Boolean inUse) {
}
