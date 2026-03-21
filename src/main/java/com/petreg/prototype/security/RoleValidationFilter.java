package com.petreg.prototype.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RoleValidationFilter extends OncePerRequestFilter {

    private static final String ACTIVE_ROLE_HEADER = "X-Active-Role";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()) {
            String claimedRole = request.getHeader(ACTIVE_ROLE_HEADER);

            // The header is required
            if (claimedRole == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    ACTIVE_ROLE_HEADER + " is required");
                return;
            }

            boolean roleIsHeld = auth.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(a -> a.equals("ROLE_" + claimedRole.toUpperCase()));

            if (!roleIsHeld) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "User does not hold role: " + claimedRole);
                return;
            }

            // Role is valid — store it in the authentication details
            // so the service layer can read it without touching the HTTP request
            ((UsernamePasswordAuthenticationToken) auth).setDetails(claimedRole.toUpperCase());
        }

        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals("/api/auth/login");
    }
}
