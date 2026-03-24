package com.petreg.prototype.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.petreg.prototype.dto.PetTransferCreateDto;
import com.petreg.prototype.dto.PetTransferResponseDto;
import com.petreg.prototype.service.PetTransferService;

@RestController
@RequestMapping("/api/pets")
public class PetTransferController {

    private PetTransferService transferService;

    public PetTransferController(PetTransferService petOwnershipTransferService) {
        this.transferService = petOwnershipTransferService;
    }

    // Create
    @PostMapping("/{petId}/transfers")
    @ResponseStatus(HttpStatus.CREATED)
    public PetTransferResponseDto create(
            @PathVariable Long petId,
            @RequestBody PetTransferCreateDto data) {
        return transferService.createTransfer(petId, data);
    }

    // Accept
    @PatchMapping("/{petId}/transfers/{id}/accept")
    @ResponseStatus(HttpStatus.OK)
    public PetTransferResponseDto accept(
            @PathVariable Long petId,
            @PathVariable Long id) {
        return transferService.resolveTransfer(petId, id, true);
    }

    // Decline
    @PatchMapping("/{petId}/transfers/{id}/decline")
    @ResponseStatus(HttpStatus.OK)
    public PetTransferResponseDto decline(
            @PathVariable Long petId,
            @PathVariable Long id) {
        return transferService.resolveTransfer(petId, id, false);
    }

    // Cancel
    @PatchMapping("/{petId}/transfers/{id}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public void cancel(
            @PathVariable Long petId,
            @PathVariable Long id) {
        transferService.cancelTransfer(petId, id);
    }
}
