package com.petreg.prototype.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.petreg.prototype.dto.PetCreateDto;
import com.petreg.prototype.dto.PetResponseDto;
import com.petreg.prototype.dto.PetUpdateDto;
import com.petreg.prototype.exception.ResourceNotFoundException;
import com.petreg.prototype.exception.BadRequestException;
import com.petreg.prototype.exception.ConflictException;
import com.petreg.prototype.mapper.PetMapper;
import com.petreg.prototype.model.Breed;
import com.petreg.prototype.model.Microchip;
import com.petreg.prototype.model.Pet;
import com.petreg.prototype.model.User;
import com.petreg.prototype.model.type.PetLifeCycleEvent;
import com.petreg.prototype.model.type.PetStatus;
import com.petreg.prototype.repository.BreedRepository;
import com.petreg.prototype.repository.MicrochipRepository;
import com.petreg.prototype.repository.PetRepository;
import com.petreg.prototype.repository.UserRepository;
import com.petreg.prototype.security.ActiveUserContext;

import jakarta.transaction.Transactional;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final BreedRepository breedRepository;
    private final MicrochipRepository microchipRepository;
    private final UserRepository userRepository;
    private final PetMapper petMapper;
    private final PetEventService petEventService;
    private final ActiveUserContext activeUserContext;

    public PetService(
            PetRepository petRepository,
            BreedRepository breedRepository,
            MicrochipRepository microchipRepository,
            UserRepository userRepository,
            PetMapper petMapper,
            PetEventService petEventService,
            ActiveUserContext activeUserContext) {
        this.petRepository = petRepository;
        this.breedRepository = breedRepository;
        this.microchipRepository = microchipRepository;
        this.userRepository = userRepository;
        this.petMapper = petMapper;
        this.petEventService = petEventService;
        this.activeUserContext = activeUserContext;
    }

    // Create
    @Transactional
    public PetResponseDto createPet(PetCreateDto dto, Authentication authentication) {

        Breed breed = breedRepository.findById(dto.breedId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Breed with id " + dto.breedId() + " not found"
            ));

        Microchip microchip = null;
        User owner = null;

        if (dto.microchipId() != null) {
            microchip = microchipRepository.findById(dto.microchipId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Microchip with id " + dto.microchipId() + " not found"
                ));

            if (microchip.getInUse()) {
                throw new ConflictException("Microchip already assigned to another pet");
            }

            microchip.setInUse(true);
        }

        if (dto.ownerId() != null) {
            owner = userRepository.findById(dto.ownerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Owner with id " + dto.ownerId() + " not found"
                ));
        }

        Pet pet = petMapper.fromDto(dto, breed, microchip, owner);
        Pet savedPet = petRepository.save(pet);

        petEventService.logEvent(
            savedPet,
            activeUserContext.getUser(),
            activeUserContext.getActiveRoleType(),
            PetLifeCycleEvent.REGISTRATION,
            null
        );

        return petMapper.toDto(savedPet);
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
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Pet with id " + id + " not found"
                ));

        return petMapper.toDto(pet);
    }

    // Update
    @Transactional
    public PetResponseDto updatePet(Long id, PetUpdateDto dto) {

        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Pet with id " + id + " not found"
                ));

        Breed breed = null;
        Microchip microchip = null;
        User owner = null;

        if (dto.breedId() != null) {
            breed = breedRepository.findById(dto.breedId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Breed with id " + dto.breedId() + " not found"
                ));
        }

        if (dto.microchipId() != null) {
            if (pet.getMicrochip() != null) {
                throw new ConflictException("Pet already has a microchip and it cannot be changed");
            }

            microchip = microchipRepository.findById(dto.microchipId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Microchip with id " + dto.microchipId() + " not found"
                ));

            if (microchip.getInUse()) {
                throw new ConflictException("Microchip already assigned to another pet");
            }

            microchip.setInUse(true);
            pet.setMicrochip(microchip);
        }

        if (dto.ownerId() != null) {
            owner = userRepository.findById(dto.ownerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Owner with id " + dto.ownerId() + " not found"
                ));
        }

        petMapper.update(dto, pet, breed, microchip, owner);
        return petMapper.toDto(petRepository.save(pet));
    }

    // Report lost pet
    @Transactional
    public PetResponseDto reportLost(Long id, String description, Authentication authentication) {
        Pet pet = petRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Pet with id " + id + " not found"
            ));

        if (pet.getStatus() == PetStatus.DECEASED) {
            throw new BadRequestException("A deceased pet cannot be reported as lost");
        }

        if (pet.getStatus() == PetStatus.EXPORTED) {
            throw new BadRequestException("An exported pet cannot be reported as lost");
        }

        if (pet.getStatus() == PetStatus.LOST) {
            throw new BadRequestException("Pet is already marked as lost");
        }

        pet.setStatus(PetStatus.LOST);
        Pet savedPet = petRepository.save(pet);

        petEventService.logEvent(
            savedPet,
            activeUserContext.getUser(),
            activeUserContext.getActiveRoleType(),
            PetLifeCycleEvent.LOST_REPORTED,
            description
        );

        return petMapper.toDto(savedPet);
    }

    // Report found pet
    @Transactional
    public PetResponseDto markFoundInShelter(Long id, String description, Authentication authentication) {
        Pet pet = petRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Pet with id " + id + " not found"
            ));

        if (pet.getStatus() == PetStatus.DECEASED) {
            throw new BadRequestException("A deceased pet cannot be marked as found");
        }

        if (pet.getStatus() == PetStatus.EXPORTED) {
            throw new BadRequestException("An exported pet cannot be marked as found");
        }

        if (pet.getStatus() != PetStatus.LOST) {
            throw new BadRequestException("Only a lost pet can be marked as found in shelter");
        }

        pet.setStatus(PetStatus.FOUND);
        Pet savedPet = petRepository.save(pet);

        petEventService.logEvent(
            savedPet,
            activeUserContext.getUser(),
            activeUserContext.getActiveRoleType(),
            PetLifeCycleEvent.FOUND_IN_SHELTER,
            description
        );

        return petMapper.toDto(savedPet);
    }

    // Return pet to owner
    @Transactional
    public PetResponseDto markReturnedToOwner(Long id, String description, Authentication authentication) {
        Pet pet = petRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Pet with id " + id + " not found"
            ));

        if (pet.getStatus() == PetStatus.DECEASED) {
            throw new BadRequestException("A deceased pet cannot be returned to owner");
        }

        if (pet.getStatus() == PetStatus.EXPORTED) {
            throw new BadRequestException("An exported pet cannot be returned to owner");
        }

        if (pet.getStatus() != PetStatus.FOUND && pet.getStatus() != PetStatus.LOST) {
            throw new BadRequestException("Only a lost or found pet can be returned to owner");
        }

        pet.setStatus(PetStatus.ACTIVE);
        Pet savedPet = petRepository.save(pet);

        petEventService.logEvent(
            savedPet,
            activeUserContext.getUser(),
            activeUserContext.getActiveRoleType(),
            PetLifeCycleEvent.RETURNED_TO_OWNER,
            description
        );

        return petMapper.toDto(savedPet);
    }

    // Report death of pet
    @Transactional
    public PetResponseDto reportDeath(Long id, String description, Authentication authentication) {
        Pet pet = petRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Pet with id " + id + " not found"
            ));

        if (pet.getStatus() == PetStatus.DECEASED) {
            throw new BadRequestException("Pet is already marked as deceased");
        }

        if (pet.getStatus() == PetStatus.EXPORTED) {
            throw new BadRequestException("An exported pet cannot be marked as deceased");
        }

        pet.setStatus(PetStatus.DECEASED);
        Pet savedPet = petRepository.save(pet);

        petEventService.logEvent(
            savedPet,
            activeUserContext.getUser(),
            activeUserContext.getActiveRoleType(),
            PetLifeCycleEvent.DEATH_REPORTED,
            description
        );

        return petMapper.toDto(savedPet);
    }

    // Report permanent export of pet
    @Transactional
    public PetResponseDto recordPermanentExport(Long id, String description, Authentication authentication) {
        Pet pet = petRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Pet with id " + id + " not found"
            ));

        if (pet.getStatus() == PetStatus.DECEASED) {
            throw new BadRequestException("A deceased pet cannot be permanently exported");
        }

        if (pet.getStatus() == PetStatus.EXPORTED) {
            throw new BadRequestException("Pet is already marked as exported");
        }

        pet.setStatus(PetStatus.EXPORTED);
        Pet savedPet = petRepository.save(pet);

        petEventService.logEvent(
            savedPet,
            activeUserContext.getUser(),
            activeUserContext.getActiveRoleType(),
            PetLifeCycleEvent.PERMANENT_EXPORT,
            description
        );

        return petMapper.toDto(savedPet);
    }

    // Delete
    @Transactional
    public void deletePet(Long id) {

        Pet pet = petRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Pet with id " + id + " not found"
            ));

        petRepository.delete(pet);
    }
}
