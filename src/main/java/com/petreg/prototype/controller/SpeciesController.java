package com.petreg.prototype.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.petreg.prototype.dto.SpeciesCreateDto;
import com.petreg.prototype.dto.SpeciesResponseDto;
import com.petreg.prototype.dto.SpeciesUpdateDto;
import com.petreg.prototype.service.SpeciesService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/species")
public class SpeciesController {

    private final SpeciesService speciesService;

    public SpeciesController(SpeciesService speciesService) {
        this.speciesService = speciesService;
    }

    // Create
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public SpeciesResponseDto create(@Valid @RequestBody SpeciesCreateDto data) {
        return speciesService.createSpecies(data);
    }

    // Retrieve one
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public SpeciesResponseDto one(@PathVariable Long id) {
        return speciesService.getSpecies(id);
    }

    // Retrieve all
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public List<SpeciesResponseDto> all() {
        return speciesService.getAllSpecies();
    }

    // Update
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public SpeciesResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody SpeciesUpdateDto data) {

        return speciesService.updateSpecies(id, data);
    }

    // Delete
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        speciesService.deleteSpecies(id);
    }
}
