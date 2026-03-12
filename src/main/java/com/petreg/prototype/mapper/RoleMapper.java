package com.petreg.prototype.mapper;

import org.springframework.stereotype.Component;

import com.petreg.prototype.dto.RoleCreateDto;
import com.petreg.prototype.dto.RoleResponseDto;
import com.petreg.prototype.dto.RoleUpdateDto;
import com.petreg.prototype.model.Role;

@Component
public class RoleMapper {

    public RoleResponseDto toDto(Role role) {
        return new RoleResponseDto(
            role.getId(),
            role.getName()
        );
    }

    public Role fromDto(RoleCreateDto dto) {
        return new Role(
            dto.name()
        );
    }

    public void update(RoleUpdateDto dto, Role role) {
        // --- Only modify non-null data fields ---

        if (dto.name() != null) {
            role.setName(dto.name());
        }
    }
}
