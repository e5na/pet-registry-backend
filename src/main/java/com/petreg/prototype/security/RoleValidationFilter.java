package com.petreg.prototype.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.petreg.prototype.model.type.RoleEnum;
import com.petreg.prototype.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RoleValidationFilter extends OncePerRequestFilter {

    private static final String ACTIVE_ROLE_HEADER = "X-Active-Role";

    private final ActiveUserContext activeUserContext;
    private final UserRepository userRepository;

    public RoleValidationFilter(
            ActiveUserContext activeUserContext,
            UserRepository userRepository) {
        this.activeUserContext = activeUserContext;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()) {
            String header = request.getHeader(ACTIVE_ROLE_HEADER);

            // The header is required
            if (header == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    ACTIVE_ROLE_HEADER + " is required");
                return;
            }

            String claimedRole = header.toUpperCase();

            boolean roleIsHeld = auth.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(a -> a.equals("ROLE_" + claimedRole));

            if (!roleIsHeld) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "User does not hold role: " + claimedRole);
                return;
            }

            // Make the active user available to other Spring managed components
            // TODO: fix redundant second call to the db to get the user entity
            userRepository.findByPersonalCodeWithRoles(auth.getName()).ifPresent(activeUserContext::setUser);
            activeUserContext.setActiveRoleType(RoleEnum.valueOf(claimedRole));
        }

        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals("/api/auth/login");
    }
}
