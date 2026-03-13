package com.petreg.prototype.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.petreg.prototype.dto.UserCreateDto;
import com.petreg.prototype.dto.UserResponseDto;
import com.petreg.prototype.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody UserCreateDto data) {
        userService.createUser(data);
    }

    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto login(Principal principal) {
        // By now, Basic Auth has already succeeded
        return userService.getUserByPersonalCode(principal.getName());
    }
}
