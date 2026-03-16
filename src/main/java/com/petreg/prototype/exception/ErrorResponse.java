package com.petreg.prototype.exception;

public record ErrorResponse(
    int status,
    String error,
    String message,
    String path,
    String timestamp
) {}
