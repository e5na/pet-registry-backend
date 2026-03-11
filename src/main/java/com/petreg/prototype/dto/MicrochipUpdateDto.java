package com.petreg.prototype.dto;

public record MicrochipUpdateDto(
    Integer chipNumber,
    String supplier,
    Boolean inUse
) {}
