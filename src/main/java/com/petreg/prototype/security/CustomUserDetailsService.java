package com.petreg.prototype.security;

import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.petreg.prototype.model.User;
import com.petreg.prototype.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String personalCode) throws UsernameNotFoundException {
        User user = userRepository.findByPersonalCode(personalCode)
            .orElseThrow(() -> new UsernameNotFoundException(
                    "User with personalCode " + personalCode + " not found"
                )
            );

        return org.springframework.security.core.userdetails.User
            .withUsername(user.getPersonalCode())
            .password(user.getPassword())
            .authorities(
                user.getRoles().stream()
                    .map(role -> "ROLE_" + role.getName().toString())
                    .collect(Collectors.toSet())
                .toArray(new String[0])
            )
            .build();
    }
}
