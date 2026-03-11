package com.petreg.prototype.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.petreg.prototype.dto.UserCreateDto;
import com.petreg.prototype.dto.UserResponseDto;
import com.petreg.prototype.dto.UserUpdateDto;
import com.petreg.prototype.mapper.UserMapper;
import com.petreg.prototype.model.User;
import com.petreg.prototype.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    // Create
    public UserResponseDto createUser(UserCreateDto input) {
        User user = userMapper.fromDto(input);
        return userMapper.toDto(userRepository.save(user));
    }

    // Retrieve
    public UserResponseDto getUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(
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
            .orElseThrow(() -> new RuntimeException(
                "User with id " + id + " not found"
            ));
        userMapper.update(dto, user);
        return userMapper.toDto(userRepository.save(user));
    }

    // Delete
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(
                "User with id " + id + " not found"
            ));
        userRepository.deleteById(id);
    }
}
