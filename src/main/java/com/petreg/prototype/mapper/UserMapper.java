package com.petreg.prototype.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.petreg.prototype.dto.OwnerProfileCreateDto;
import com.petreg.prototype.dto.OwnerProfileResponseDto;
import com.petreg.prototype.dto.OwnerProfileUpdateDto;
import com.petreg.prototype.dto.UserCreateDto;
import com.petreg.prototype.dto.UserResponseDto;
import com.petreg.prototype.dto.UserUpdateDto;
import com.petreg.prototype.model.OwnerProfile;
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
            user.getRoles().stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toSet()),
            OwnerProfileMapper.toDto(user.getOwnerProfile())
        );
    }

    public User fromDto(UserCreateDto dto) {
        User user = new User(
            dto.personalCode(),
            dto.firstName(),
            dto.lastName(),
            dto.email(),
            dto.phoneNumber(),
            OwnerProfileMapper.fromDto(dto.ownerProfile())
        );
        // This back link is crucial
        // Otherwise Hibernate will try to add a null User to the OwnerProfile which
        // would trigger an IdentifierGenerationException
        user.getOwnerProfile().setUser(user);
        return user;
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

        if (dto.ownerProfile() != null) {
            // Update already existing profile
            if (user.getOwnerProfile() != null) {
                OwnerProfileMapper.update(dto.ownerProfile(), user.getOwnerProfile());
            }
            // Note that if the user had no profile (getOwnerProfile() == null) then nothing would
            // be changed here. This might or might not be what we want
        }
    }

    public class OwnerProfileMapper {

        public static OwnerProfileResponseDto toDto(OwnerProfile ownerProfile) {
            if (ownerProfile == null) {
                return null;
            }

            return new OwnerProfileResponseDto(
                ownerProfile.getAddress()
            );
        }

        public static OwnerProfile fromDto(OwnerProfileCreateDto dto) {
            if (dto == null) {
                return null;
            }

            return new OwnerProfile(
                dto.address()
            );
        }

        public static void update(OwnerProfileUpdateDto dto, OwnerProfile ownerProfile) {
            if (dto == null || ownerProfile == null) {
                return;
            }

            if (dto.address() != null) {
                ownerProfile.setAddress(dto.address());
            }
        }
    }
}
