package com.petreg.prototype.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.petreg.prototype.dto.UserCreateDto;
import com.petreg.prototype.dto.UserResponseDto;
import com.petreg.prototype.service.AuthService;
import com.petreg.prototype.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody UserCreateDto data) {
        userService.createUser(data);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto login(Authentication auth) {
        return authService.login(auth);
    }
}
