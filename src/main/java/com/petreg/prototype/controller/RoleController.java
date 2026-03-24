package com.petreg.prototype.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.petreg.prototype.dto.RoleCreateDto;
import com.petreg.prototype.dto.RoleResponseDto;
import com.petreg.prototype.dto.RoleUpdateDto;
import com.petreg.prototype.service.RoleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    // --- Basic CRUD endpoints ---

    // Create
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public RoleResponseDto create(@Valid @RequestBody RoleCreateDto data) {
        return roleService.createRole(data);
    }

    // Retrieve
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RoleResponseDto one(@PathVariable Long id) {
        return roleService.getRole(id);
    }

    // Retrieve all
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<RoleResponseDto> all() {
        return roleService.getAllRoles();
    }

    // Update
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RoleResponseDto update(@PathVariable Long id,
            @Valid @RequestBody RoleUpdateDto data) {
        return roleService.updateRole(id, data);
    }

    // Delete
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        roleService.deleteRole(id);
    }
}
