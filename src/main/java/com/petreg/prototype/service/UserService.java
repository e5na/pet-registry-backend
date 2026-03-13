package com.petreg.prototype.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.petreg.prototype.dto.UserCreateDto;
import com.petreg.prototype.dto.UserResponseDto;
import com.petreg.prototype.dto.UserUpdateDto;
import com.petreg.prototype.exception.ResourceNotFoundException;
import com.petreg.prototype.mapper.UserMapper;
import com.petreg.prototype.model.Role;
import com.petreg.prototype.model.User;
import com.petreg.prototype.model.type.RoleEnum;
import com.petreg.prototype.repository.RoleRepository;
import com.petreg.prototype.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, UserMapper userMapper,
            RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
    }

    // Create
    public UserResponseDto createUser(UserCreateDto input) {
        if (userRepository.existsByPersonalCode(input.personalCode())) {
            throw new RuntimeException(
                "User already exists with personal code: " + input.personalCode()
            );
        }

        User user = userMapper.fromDto(input);

        // --- Assign roles based on profiles ---

        Set<RoleEnum> profileRoles = new HashSet<>();

        if (input.ownerProfile() != null) {
            profileRoles.add(RoleEnum.OWNER);
        }

        profileRoles.forEach(profileRole -> {
            Role role = roleRepository.findByName(profileRole)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Role with name " + profileRole + " not found"
                ));
            user.getRoles().add(role);
        });

        return userMapper.toDto(userRepository.save(user));
    }

    // Retrieve
    public UserResponseDto getUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "User with id " + id + " not found"
            ));
        return userMapper.toDto(user);
    }

    // Retrieve all
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
            .map(userMapper::toDto)
            .collect(Collectors.toList());
    }

    // Update
    public UserResponseDto updateUser(Long id, UserUpdateDto dto) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "User with id " + id + " not found"
            ));
        userMapper.update(dto, user);
        return userMapper.toDto(userRepository.save(user));
    }

    // Delete
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "User with id " + id + " not found"
            ));
        userRepository.delete(user);
    }

    // Retrieve by personal code
    public UserResponseDto getUserByPersonalCode(String personalCode) {
        User user = userRepository.findByPersonalCode(personalCode)
            .orElseThrow(() -> new RuntimeException(
                "User with personal code " + personalCode + " not found"
            ));
        return userMapper.toDto(user);
    }
}
