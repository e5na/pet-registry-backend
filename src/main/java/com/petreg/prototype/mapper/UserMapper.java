package com.petreg.prototype.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.petreg.prototype.dto.UserCreateDto;
import com.petreg.prototype.dto.UserResponseDto;
import com.petreg.prototype.dto.UserUpdateDto;
import com.petreg.prototype.model.User;

@Component
public class UserMapper {

    private final RoleMapper roleMapper;

    public UserMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    public UserResponseDto toDto(User user) {
        return new UserResponseDto(
            user.getId(),
            user.getPersonalCode(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPhoneNumber(),
            user.getAddress(),
            user.getRoles().stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toSet())
        );
    }

    public User fromDto(UserCreateDto dto) {
        return new User(
            dto.personalCode(),
            dto.firstName(),
            dto.lastName(),
            dto.email(),
            dto.phoneNumber(),
            dto.address()
        );
    }

    public void update(UserUpdateDto dto, User user) {
        // --- Only modify non-null data fields ---

        if (dto.personalCode() != null) {
            user.setPersonalCode(dto.personalCode());
        }

        if (dto.firstName() != null) {
            user.setFirstName(dto.firstName());
        }

        if (dto.lastName() != null) {
            user.setLastName(dto.lastName());
        }

        if (dto.email() != null) {
            user.setEmail(dto.email());
        }

        if (dto.phoneNumber() != null) {
            user.setPhoneNumber(dto.phoneNumber());
        }

        if (dto.address() != null) {
            user.setAddress(dto.address());
        }
    }
}
