package com.petreg.prototype.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.petreg.prototype.dto.MicrochipCreateDto;
import com.petreg.prototype.dto.MicrochipResponseDto;
import com.petreg.prototype.dto.MicrochipUpdateDto;
import com.petreg.prototype.exception.ResourceNotFoundException;
import com.petreg.prototype.mapper.MicrochipMapper;
import com.petreg.prototype.model.Microchip;
import com.petreg.prototype.repository.MicrochipRepository;

@Service
public class MicrochipService {

    private final MicrochipRepository microchipRepository;
    private final MicrochipMapper microchipMapper;

    public MicrochipService(MicrochipRepository microchipRepository,
            MicrochipMapper microchipMapper) {
        this.microchipRepository = microchipRepository;
        this.microchipMapper = microchipMapper;
    }

    // Create
    public MicrochipResponseDto createMicrochip(MicrochipCreateDto input) {
        Microchip microchip = microchipMapper.fromDto(input);
        return microchipMapper.toDto(microchipRepository.save(microchip));
    }

    // Retrieve
    public MicrochipResponseDto getMicrochip(Long id) {
        Microchip microchip = microchipRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Microchip with id " + id + " not found"
            ));
        return microchipMapper.toDto(microchip);
    }

    // Retrieve all
    public List<MicrochipResponseDto> getAllMicrochips() {
        return microchipRepository.findAll().stream()
            .map(microchipMapper::toDto)
            .collect(Collectors.toList());
    }

    // Search by Microchip number
    public List<MicrochipResponseDto> searchMicrochips(String chipNumber) {

        return microchipRepository
            .findByChipNumberContainingIgnoreCase(chipNumber)
            .stream()
            .map(microchipMapper::toDto)
            .toList();
}

    // Update
    public MicrochipResponseDto updateMicrochip(Long id, MicrochipUpdateDto dto) {
        Microchip microchip = microchipRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Microchip with id " + id + " not found"
            ));
        microchipMapper.update(dto, microchip);
        return microchipMapper.toDto(microchipRepository.save(microchip));
    }

    // Delete
    public void deleteMicrochip(Long id) {
        Microchip microchip = microchipRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Microchip with id " + id + " not found"
            ));
        microchipRepository.delete(microchip);
    }
}
