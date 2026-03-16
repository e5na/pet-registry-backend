package com.petreg.prototype.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.petreg.prototype.dto.SpeciesCreateDto;
import com.petreg.prototype.dto.SpeciesResponseDto;
import com.petreg.prototype.dto.SpeciesUpdateDto;
import com.petreg.prototype.exception.ResourceNotFoundException;
import com.petreg.prototype.mapper.SpeciesMapper;
import com.petreg.prototype.model.Species;
import com.petreg.prototype.repository.SpeciesRepository;

import jakarta.transaction.Transactional;

@Service
public class SpeciesService {

    private final SpeciesRepository speciesRepository;
    private final SpeciesMapper speciesMapper;

    public SpeciesService(SpeciesRepository speciesRepository, SpeciesMapper speciesMapper) {
        this.speciesRepository = speciesRepository;
        this.speciesMapper = speciesMapper;
    }

    // Create
    @Transactional
    public SpeciesResponseDto createSpecies(SpeciesCreateDto dto) {
        Species species = speciesMapper.fromDto(dto);
        return speciesMapper.toDto(speciesRepository.save(species));
    }

    // Retreive all
    public List<SpeciesResponseDto> getAllSpecies() {

        return speciesRepository.findAll()
            .stream()
            .map(speciesMapper::toDto)
            .toList();
    }

    // Retrieve by ID
    public SpeciesResponseDto getSpecies(Long id) {
        Species species = speciesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Species with id " + id + " not found"
            ));

        return speciesMapper.toDto(species);
    }

    // Update
    @Transactional
    public SpeciesResponseDto updateSpecies(Long id, SpeciesUpdateDto dto) {
        Species species = speciesRepository.findById(id)
             .orElseThrow(() -> new ResourceNotFoundException(
                "Species with id " + id + " not found"
            ));

        speciesMapper.update(dto, species);
        return speciesMapper.toDto(species);
    }

    // Delete
    @Transactional
    public void deleteSpecies(Long id) {

        Species species = speciesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Species with id " + id + " not found"
            ));

        speciesRepository.delete(species);
    }
}
