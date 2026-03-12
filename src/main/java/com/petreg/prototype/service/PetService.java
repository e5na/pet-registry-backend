package com.petreg.prototype.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.petreg.prototype.dto.PetCreateDto;
import com.petreg.prototype.dto.PetResponseDto;
import com.petreg.prototype.dto.PetUpdateDto;
import com.petreg.prototype.mapper.PetMapper;
import com.petreg.prototype.model.Breed;
import com.petreg.prototype.model.Microchip;
import com.petreg.prototype.model.Pet;
import com.petreg.prototype.model.Species;
import com.petreg.prototype.model.User;
import com.petreg.prototype.repository.BreedRepository;
import com.petreg.prototype.repository.MicrochipRepository;
import com.petreg.prototype.repository.PetRepository;
import com.petreg.prototype.repository.SpeciesRepository;
import com.petreg.prototype.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final SpeciesRepository speciesRepository;
    private final BreedRepository breedRepository;
    private final MicrochipRepository microchipRepository;
    private final UserRepository userRepository;
    private final PetMapper petMapper;

    public PetService(
        PetRepository petRepository,
        SpeciesRepository speciesRepository,
        BreedRepository breedRepository,
        MicrochipRepository microchipRepository,
        UserRepository userRepository,
        PetMapper petMapper
    ) {
        this.petRepository = petRepository;
        this.speciesRepository = speciesRepository;
        this.breedRepository = breedRepository;
        this.microchipRepository = microchipRepository;
        this.userRepository = userRepository;
        this.petMapper = petMapper;
    }

    // Create
    @Transactional
    public PetResponseDto createPet(PetCreateDto dto) {

        Species species = speciesRepository.findById(dto.speciesId())
            .orElseThrow();

        Breed breed = breedRepository.findById(dto.breedId())
            .orElseThrow();

        Microchip microchip = null;
        User owner = null;

        if (dto.microchipId() != null) {
            microchip = microchipRepository.findById(dto.microchipId())
                .orElseThrow();

            if (microchip.getInUse()) {
                throw new RuntimeException("Microchip already assigned to another pet");
            }

            microchip.setInUse(true);
        }

        if (dto.ownerId() != null) {
            owner = userRepository.findById(dto.ownerId())
                .orElseThrow();
        }

        Pet pet = petMapper.fromDto(dto, species, breed, microchip, owner);
        return petMapper.toDto(petRepository.save(pet));
    }

    // Retrieve all
    public List<PetResponseDto> getAllPets() {

        return petRepository.findAll()
                .stream()
                .map(petMapper::toDto)
                .toList();
    }

    // Retrieve by ID
    public PetResponseDto getPet(Long id) {

        Pet pet = petRepository
                .findById(id)
                .orElseThrow();

        return petMapper.toDto(pet);
    }

    // Update
    @Transactional
    public PetResponseDto updatePet(Long id, PetUpdateDto dto) {

        Pet pet = petRepository.findById(id)
                .orElseThrow();

        Species species = null;
        Breed breed = null;
        Microchip microchip = null;
        User owner = null;

        if (dto.speciesId() != null) {
            species = speciesRepository.findById(dto.speciesId())
                .orElseThrow();
        }

        if (dto.breedId() != null) {
            breed = breedRepository.findById(dto.breedId())
                .orElseThrow();
        }

        if (dto.microchipId() != null) {
            if (pet.getMicrochip() != null) {
                throw new RuntimeException("Pet already has a microchip and it cannot be changed");
            }

            microchip = microchipRepository.findById(dto.microchipId())
                .orElseThrow();

            if (microchip.getInUse()) {
                throw new RuntimeException("Microchip already assigned to another pet");
            }

            microchip.setInUse(true);
            pet.setMicrochip(microchip);
        }

        if (dto.ownerId() != null) {
            owner = userRepository.findById(dto.ownerId())
                .orElseThrow();
        }

        petMapper.update(dto, pet, species, breed, microchip, owner);
        return petMapper.toDto(petRepository.save(pet));
    }

    // Delete
    @Transactional
    public void deletePet(Long id) {

        if (!petRepository.existsById(id)) {
            throw new RuntimeException("Pet with id " + id + " not found");
        }

        petRepository.deleteById(id);
    }
}
