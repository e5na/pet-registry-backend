package com.petreg.prototype.dto;

public record PictureResponseDto(
        Long id,
        String fileName,
        byte[] picture) {
}
