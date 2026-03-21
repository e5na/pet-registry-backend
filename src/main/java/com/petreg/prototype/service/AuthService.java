package com.petreg.prototype.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.petreg.prototype.dto.LoginRequestDto;
import com.petreg.prototype.dto.UserResponseDto;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthService(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    public UserResponseDto login(LoginRequestDto data) {
        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(data.personalCode(), data.password())
        );

        // Get the principal
        // Note that User here is from security.core.userdetails, not from petreg.prototype.model
        User principal = (User) authentication.getPrincipal();

        // Now we can verify if the user has the correct profile (if set in the login request)
        if (data.roleType() != null) {
            boolean hasRole = principal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(
                        "ROLE_" + data.roleType().toString().toUpperCase()));

            // No match, so reject the login attempt
            if (!hasRole) {
                // 401 with custom error message
                throw new AccessDeniedException("No such profile");
            }
        }

        return userService.getUserByPersonalCode(principal.getUsername());
    }
}
