package com.petreg.prototype.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
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
    public BreedResponseDto create(@Valid @RequestBody BreedCreateDto data) {
        return breedService.createBreed(data);
    }

    // Retrieve one
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BreedResponseDto one(@PathVariable Long id) {
        return breedService.getBreed(id);
    }

    // Retrieve all
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<BreedResponseDto> all(@RequestParam(required = false) Long speciesId) {
        if (speciesId != null) {
            return breedService.getBreedsBySpecies(speciesId);
        }

        return breedService.getAllBreeds();
    }

    // Update
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BreedResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody BreedUpdateDto data) {

        return breedService.updateBreed(id, data);
    }

    // Delete
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        breedService.deleteBreed(id);
    }
}
