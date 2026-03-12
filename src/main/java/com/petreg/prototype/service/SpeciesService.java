package com.petreg.prototype.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.petreg.prototype.dto.SpeciesCreateDto;
import com.petreg.prototype.dto.SpeciesResponseDto;
import com.petreg.prototype.dto.SpeciesUpdateDto;
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
            .orElseThrow();

        return speciesMapper.toDto(species);
    }

    // Update
    @Transactional
    public SpeciesResponseDto updateSpecies(Long id, SpeciesUpdateDto dto) {
        Species species = speciesRepository.findById(id)
            .orElseThrow();

        speciesMapper.update(dto, species);
        return speciesMapper.toDto(species);
    }

    // Delete
    @Transactional
    public void deleteSpecies(Long id) {

        if (!speciesRepository.existsById(id)) {
            throw new RuntimeException("Species with id " + id + " not found");
        }

        speciesRepository.deleteById(id);
    }
}
