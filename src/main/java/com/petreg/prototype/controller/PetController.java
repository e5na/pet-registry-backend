package com.petreg.prototype.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.petreg.prototype.dto.PetCreateDto;
import com.petreg.prototype.dto.PetEventResponseDto;
import com.petreg.prototype.dto.PetResponseDto;
import com.petreg.prototype.dto.PetUpdateDto;
import com.petreg.prototype.service.PetService;
import com.petreg.prototype.service.PetEventService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    private final PetService petService;
    private final PetEventService petEventService;

    public PetController(PetService petService, PetEventService petEventService) {
        this.petService = petService;
        this.petEventService = petEventService;
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

    // Retrieve pet history
    @GetMapping("/{id}/history")
    @ResponseStatus(HttpStatus.OK)
    public List<PetEventResponseDto> history(@PathVariable Long id) {
        return petEventService.getPetHistory(id);
    }

    // Update
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PetResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody PetUpdateDto data) {

        return petService.updatePet(id, data);
    }

    // Report lost pet
    @PatchMapping("/{id}/report-lost")
    @ResponseStatus(HttpStatus.OK)
    public PetResponseDto reportLost(@PathVariable Long id) {
        return petService.reportLost(id, null);
    }

    // Report found pet
    @PatchMapping("/{id}/mark-found")
    @ResponseStatus(HttpStatus.OK)
    public PetResponseDto markFound(@PathVariable Long id) {
        return petService.markFoundInShelter(id, null);
    }

    // Return pet to owner
    @PatchMapping("/{id}/return-to-owner")
    @ResponseStatus(HttpStatus.OK)
    public PetResponseDto returnToOwner(@PathVariable Long id) {
        return petService.markReturnedToOwner(id, null);
    }

    // Report death of pet
    @PatchMapping("/{id}/report-death")
    @ResponseStatus(HttpStatus.OK)
    public PetResponseDto reportDeath(@PathVariable Long id) {
        return petService.reportDeath(id, null);
    }

    // Report permanent export of pet
    @PatchMapping("/{id}/export")
    @ResponseStatus(HttpStatus.OK)
    public PetResponseDto exportPet(@PathVariable Long id) {
        return petService.recordPermanentExport(id, null);
    }

    // Delete
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        petService.deletePet(id);
    }
}
