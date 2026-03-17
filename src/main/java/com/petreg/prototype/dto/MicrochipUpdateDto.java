package com.petreg.prototype.dto;

public record MicrochipUpdateDto(
    String chipNumber,
    String supplier,
    Boolean inUse
) {}
