package com.petreg.prototype.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.petreg.prototype.exception.BadRequestException;
import com.petreg.prototype.exception.ResourceNotFoundException;
import com.petreg.prototype.model.Role;
import com.petreg.prototype.model.User;
import com.petreg.prototype.model.type.RoleEnum;
import com.petreg.prototype.repository.UserRepository;
import com.petreg.prototype.security.ActiveRoleContext;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;
    private final ActiveRoleContext activeRoleContext;

    public CurrentUserService(
        UserRepository userRepository,
        ActiveRoleContext activeRoleContext
    ) {
        this.userRepository = userRepository;
        this.activeRoleContext = activeRoleContext;
    }

    public User getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadRequestException("No authenticated user found");
        }

        String personalCode = authentication.getName();

        if (personalCode == null || personalCode.isBlank() || "anonymousUser".equals(personalCode)) {
            throw new BadRequestException("Invalid authenticated user");
        }

        return userRepository.findByPersonalCode(personalCode)
            .orElseThrow(() -> new ResourceNotFoundException(
                "User not found with personal code: " + personalCode
            ));
    }

    public RoleEnum getActiveRoleEnum() {
        RoleEnum activeRole = activeRoleContext.getActiveRole();

        if (activeRole == null) {
            throw new BadRequestException("Active role is required");
        }

        return activeRole;
    }

    public Role getActiveRole(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        RoleEnum activeRole = getActiveRoleEnum();

        return user.getRoles().stream()
            .filter(role -> role.getName() == activeRole)
            .findFirst()
            .orElseThrow(() -> new BadRequestException(
                "User does not have role: " + activeRole
            ));
    }
}
