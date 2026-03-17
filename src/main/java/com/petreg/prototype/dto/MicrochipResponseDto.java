package com.petreg.prototype.dto;

public record MicrochipResponseDto(
    Long id,
    String chipNumber,
    String supplier,
    boolean inUse
) {}
