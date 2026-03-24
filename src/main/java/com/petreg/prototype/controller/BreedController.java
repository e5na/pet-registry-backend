package com.petreg.prototype.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.petreg.prototype.dto.BreedCreateDto;
import com.petreg.prototype.dto.BreedResponseDto;
import com.petreg.prototype.dto.BreedUpdateDto;
import com.petreg.prototype.service.BreedService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/breeds")
public class BreedController {

    private final BreedService breedService;

    public BreedController(BreedService breedService) {
        this.breedService = breedService;
    }

    // Create
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public BreedResponseDto create(@Valid @RequestBody BreedCreateDto data) {
        return breedService.createBreed(data);
    }

    // Retrieve one
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public BreedResponseDto one(@PathVariable Long id) {
        return breedService.getBreed(id);
    }

    // Retrieve all
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public List<BreedResponseDto> all(@RequestParam(required = false) Long speciesId) {
        if (speciesId != null) {
            return breedService.getBreedsBySpecies(speciesId);
        }

        return breedService.getAllBreeds();
    }

    // Update
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public BreedResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody BreedUpdateDto data) {

        return breedService.updateBreed(id, data);
    }

    // Delete
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        breedService.deleteBreed(id);
    }
}
