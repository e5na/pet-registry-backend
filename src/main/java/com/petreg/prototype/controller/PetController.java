package com.petreg.prototype.controller;

import java.util.List;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

import com.petreg.prototype.dto.PetCreateDto;
import com.petreg.prototype.dto.PetResponseDto;
import com.petreg.prototype.dto.PetUpdateDto;
import com.petreg.prototype.service.PetService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    // Create
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public PetResponseDto create(@Valid @RequestBody PetCreateDto data) {
        return petService.createPet(data);
    }

    // Retrieve one
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PetResponseDto one(@PathVariable Long id) {
        return petService.getPet(id);
    }

    // Retrieve all
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<PetResponseDto> all() {
        return petService.getAllPets();
    }

    // Update
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PetResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody PetUpdateDto data) {

        return petService.updatePet(id, data);
    }

    // Delete
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        petService.deletePet(id);
    }
}
