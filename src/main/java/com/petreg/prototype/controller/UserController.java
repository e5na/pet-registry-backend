package com.petreg.prototype.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.petreg.prototype.dto.PetResponseDto;
import com.petreg.prototype.dto.UserCreateDto;
import com.petreg.prototype.dto.UserResponseDto;
import com.petreg.prototype.dto.UserUpdateDto;
import com.petreg.prototype.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // --- Basic CRUD endpoints ---

    // Create
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto create(@Valid @RequestBody UserCreateDto data) {
        return userService.createUser(data);
    }

    // Retrieve
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public UserResponseDto one(@PathVariable Long id) {
        return userService.getUser(id);
    }

    // Retrieve all
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseDto> all() {
        return userService.getAllUsers();
    }

    // Update
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public UserResponseDto update(@PathVariable Long id,
            @Valid @RequestBody UserUpdateDto data) {
        return userService.updateUser(id, data);
    }

    // Delete
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    // Retrieve pets
    @GetMapping("/{id}/pets")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public List<PetResponseDto> getPets(@PathVariable Long id) {
        return userService.getUserPets(id);
    }
}
