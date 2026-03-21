package com.petreg.prototype.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.petreg.prototype.dto.OwnerProfileCreateDto;
import com.petreg.prototype.dto.OwnerProfileResponseDto;
import com.petreg.prototype.dto.OwnerProfileUpdateDto;
import com.petreg.prototype.dto.UserCreateDto;
import com.petreg.prototype.dto.UserResponseDto;
import com.petreg.prototype.dto.UserUpdateDto;
import com.petreg.prototype.dto.VetProfileCreateDto;
import com.petreg.prototype.dto.VetProfileResponseDto;
import com.petreg.prototype.dto.VetProfileUpdateDto;
import com.petreg.prototype.model.OwnerProfile;
import com.petreg.prototype.model.User;
import com.petreg.prototype.model.VetProfile;

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
            OwnerProfileMapper.toDto(user.getOwnerProfile()),
            VetProfileMapper.toDto(user.getVetProfile())
        );
    }

    public User fromDto(UserCreateDto dto) {
        User user = new User(
            dto.personalCode(),
            dto.firstName(),
            dto.lastName(),
            dto.email(),
            dto.phoneNumber(),
            dto.password(),
            OwnerProfileMapper.fromDto(dto.ownerProfile()),
            VetProfileMapper.fromDto(dto.vetProfile())
        );

        if (user.getOwnerProfile() != null) {
            // This back link is crucial
            // Otherwise Hibernate will try to add a null User to the OwnerProfile which
            // would trigger an IdentifierGenerationException
            user.getOwnerProfile().setUser(user);
        }

        if (user.getVetProfile() != null) {
            // This back link is crucial
            // Otherwise Hibernate will try to add a null User to the VetProfile which
            // would trigger an IdentifierGenerationException
            user.getVetProfile().setUser(user);
        }

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

        // No password update here, this is intentional

        if (dto.ownerProfile() != null) {
            // Update already existing profile
            if (user.getOwnerProfile() != null) {
                OwnerProfileMapper.update(dto.ownerProfile(), user.getOwnerProfile());
            }
            // Note that if the user had no profile (getOwnerProfile() == null) then nothing would
            // be changed here. This might or might not be what we want
        }

        if (dto.vetProfile() != null) {
            // Update already existing profile
            if (user.getVetProfile() != null) {
                VetProfileMapper.update(dto.vetProfile(), user.getVetProfile());
            }
            // Note that if the user had no profile (getVetProfile() == null) then nothing would
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

    public class VetProfileMapper {

        public static VetProfileResponseDto toDto(VetProfile vetProfile) {
            if (vetProfile == null) {
                return null;
            }

            return new VetProfileResponseDto(
                vetProfile.getLicenseNumber(),
                vetProfile.getSpecialization()
            );
        }

        public static VetProfile fromDto(VetProfileCreateDto dto) {
            if (dto == null) {
                return null;
            }

            return new VetProfile(
                dto.licenseNumber(),
                dto.specialization()
            );
        }

        public static void update(VetProfileUpdateDto dto, VetProfile vetProfile) {
            if (dto == null || vetProfile == null) {
                return;
            }

            if (dto.licenseNumber() != null) {
                vetProfile.setLicenseNumber(dto.licenseNumber());
            }

            if (dto.specialization() != null) {
                vetProfile.setSpecialization(dto.specialization());
            }
        }
    }
}
