package com.petreg.prototype.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.petreg.prototype.dto.UserResponseDto;

@Service
public class AuthService {

    private final UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public UserResponseDto login(Authentication auth) {
        return userService.getUserByPersonalCode(auth.getName());
    }
}
