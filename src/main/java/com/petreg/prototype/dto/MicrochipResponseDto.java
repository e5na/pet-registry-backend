package com.petreg.prototype.dto;

public record MicrochipResponseDto(
    Long id,
    int chipNumber,
    String supplier,
    boolean inUse
) {}
