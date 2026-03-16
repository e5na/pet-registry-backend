package com.petreg.prototype.exception;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import tools.jackson.databind.exc.InvalidFormatException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
        DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private ResponseEntity<ErrorResponse> buildResponse(
        HttpStatus status,
        String message,
        HttpServletRequest request
    ) {
        ErrorResponse response = new ErrorResponse(
            status.value(),
            status.getReasonPhrase(),
            message,
            request.getRequestURI(),
            LocalDateTime.now().format(TIMESTAMP_FORMATTER)
        );

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
        ResourceNotFoundException ex,
        HttpServletRequest request
    ) {

        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(
        BadRequestException ex,
        HttpServletRequest request
    ) {

        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(
        ConflictException ex,
        HttpServletRequest request
    ) {

        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
        MethodArgumentNotValidException ex,
        HttpServletRequest request
    ) {

        String message = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(fieldError -> fieldError.getDefaultMessage())
            .collect(Collectors.joining("; "));

        return buildResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadableMessage(
        HttpMessageNotReadableException ex,
        HttpServletRequest request) {

        String message = "Invalid request body";

        Throwable cause = ex.getCause();

        if (cause instanceof InvalidFormatException formatEx) {

            String field = formatEx.getPath()
                .stream()
                .map(ref -> ref.getPropertyName())
                .filter(Objects::nonNull)
                .findFirst()
                .orElse("unknown");

            if (formatEx.getTargetType().equals(LocalDate.class)) {
                message = "Field '%s' must be a date in format YYYY-MM-DD".formatted(field);
            } else {
                message = "Invalid value for field '%s'".formatted(field);
            }
        }

        return buildResponse(HttpStatus.BAD_REQUEST, message, request);
    }
}
