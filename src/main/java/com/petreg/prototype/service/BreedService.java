package com.petreg.prototype.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.petreg.prototype.dto.BreedCreateDto;
import com.petreg.prototype.dto.BreedResponseDto;
import com.petreg.prototype.dto.BreedUpdateDto;
import com.petreg.prototype.exception.ResourceNotFoundException;
import com.petreg.prototype.mapper.BreedMapper;
import com.petreg.prototype.model.Breed;
import com.petreg.prototype.model.Species;
import com.petreg.prototype.repository.BreedRepository;
import com.petreg.prototype.repository.SpeciesRepository;

import jakarta.transaction.Transactional;

@Service
public class BreedService {

    private final BreedRepository breedRepository;
    private final SpeciesRepository speciesRepository;
    private final BreedMapper breedMapper;

    public BreedService(
        BreedRepository breedRepository,
        SpeciesRepository speciesRepository,
        BreedMapper breedMapper
    ) {
        this.breedRepository = breedRepository;
        this.speciesRepository = speciesRepository;
        this.breedMapper = breedMapper;
    }

    // Create
    @Transactional
    public BreedResponseDto createBreed(BreedCreateDto dto) {

        Species species = speciesRepository.findById(dto.speciesId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Species with id " + dto.speciesId() + " not found"
            ));

        Breed breed = breedMapper.fromDto(dto, species);
        return breedMapper.toDto(breedRepository.save(breed));
    }

    // Retrieve all
    public List<BreedResponseDto> getAllBreeds() {

    return breedRepository.findAll()
            .stream()
            .map(breedMapper::toDto)
            .toList();
    }

    // Retrieve all for species
    public List<BreedResponseDto> getBreedsBySpecies(Long speciesId) {
        speciesRepository.findById(speciesId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Species with id " + speciesId + " not found"
            ));

        return breedRepository.findBySpecies_Id(speciesId)
            .stream()
            .map(breedMapper::toDto)
            .toList();
    }

    // Retrieve by ID
    public BreedResponseDto getBreed(Long id) {

        Breed breed = breedRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Breed with id " + id + " not found"
            ));

        return breedMapper.toDto(breed);
    }

    // Update
    @Transactional
    public BreedResponseDto updateBreed(Long id, BreedUpdateDto dto) {

        Breed breed = breedRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Breed with id " + id + " not found"
            ));

        Species species = null;

        if (dto.speciesId() != null) {
            species = speciesRepository.findById(dto.speciesId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Species with id " + dto.speciesId() + " not found"
                ));
        }

        breedMapper.update(dto, breed, species);
        return breedMapper.toDto(breedRepository.save(breed));
    }

    // Delete
    @Transactional
    public void deleteBreed(Long id) {

        if (!breedRepository.existsById(id)) {
            throw new ResourceNotFoundException("Breed with id " + id + " not found");
        }

        breedRepository.deleteById(id);
    }
}
