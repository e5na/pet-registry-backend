package com.petreg.prototype.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.petreg.prototype.dto.MicrochipCreateDto;
import com.petreg.prototype.dto.MicrochipResponseDto;
import com.petreg.prototype.dto.MicrochipUpdateDto;
import com.petreg.prototype.service.MicrochipService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/microchips")
public class MicrochipController {

    private final MicrochipService microchipService;

    public MicrochipController(MicrochipService microchipService) {
        this.microchipService = microchipService;
    }

    // --- Basic CRUD endpoints ---

    // Create
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('VET', 'ADMIN')")
    public MicrochipResponseDto create(@Valid @RequestBody MicrochipCreateDto data) {
        return microchipService.createMicrochip(data);
    }

    // Retrieve
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('OWNER', 'VET', 'ADMIN')")
    public MicrochipResponseDto one(@PathVariable Long id) {
        return microchipService.getMicrochip(id);
    }

    // Retrieve all
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('VET', 'ADMIN')")
    public List<MicrochipResponseDto> all(
        @RequestParam(required = false) String chipNumber
    ) {
        if (chipNumber == null || chipNumber.isBlank()) {
            return microchipService.getAllMicrochips();
        }

        return microchipService.searchMicrochips(chipNumber);
    }

    // Update
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public MicrochipResponseDto update(@PathVariable Long id,
            @Valid @RequestBody MicrochipUpdateDto data) {
        return microchipService.updateMicrochip(id, data);
    }

    // Delete
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        microchipService.deleteMicrochip(id);
    }
}
