package com.petreg.prototype.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.petreg.prototype.dto.RoleCreateDto;
import com.petreg.prototype.dto.RoleResponseDto;
import com.petreg.prototype.dto.RoleUpdateDto;
import com.petreg.prototype.exception.ResourceNotFoundException;
import com.petreg.prototype.mapper.RoleMapper;
import com.petreg.prototype.model.Role;
import com.petreg.prototype.repository.RoleRepository;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleService(RoleRepository roleRepository,
            RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    // Create
    public RoleResponseDto createRole(RoleCreateDto input) {
        Role role = roleMapper.fromDto(input);
        return roleMapper.toDto(roleRepository.save(role));
    }

    // Retrieve
    public RoleResponseDto getRole(Long id) {
        Role role = roleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Role with id " + id + " not found"
            ));
        return roleMapper.toDto(role);
    }

    // Retrieve all
    public List<RoleResponseDto> getAllRoles() {
        return roleRepository.findAll().stream()
            .map(roleMapper::toDto)
            .collect(Collectors.toList());
    }

    // Update
    public RoleResponseDto updateRole(Long id, RoleUpdateDto dto) {
        Role role = roleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Role with id " + id + " not found"
            ));
        roleMapper.update(dto, role);
        return roleMapper.toDto(roleRepository.save(role));
    }

    // Delete
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Role with id " + id + " not found"
            ));
        roleRepository.delete(role);
    }
}
