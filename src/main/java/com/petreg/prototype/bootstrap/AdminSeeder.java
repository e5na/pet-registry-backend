package com.petreg.prototype.bootstrap;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.petreg.prototype.exception.ResourceNotFoundException;
import com.petreg.prototype.model.Role;
import com.petreg.prototype.model.User;
import com.petreg.prototype.model.type.RoleEnum;
import com.petreg.prototype.repository.RoleRepository;
import com.petreg.prototype.repository.UserRepository;

@Component
public class AdminSeeder {

    // Lo and behold, the uncrackable password!
    private static final String DEFAULT_PASSWORD = "admin123";

    private static final Logger log = LoggerFactory.getLogger(AdminSeeder.class);

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public AdminSeeder(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ContextRefreshedEvent.class)
    @Order(2)
    @Transactional
    public void run() {
        createAdmin();
    }

    public void createAdmin() {
        log.info("Bootstrapping administrator account...");
        RoleEnum adminRole = RoleEnum.ADMIN;
        Role role = roleRepository.findByName(adminRole)
                // Should not happen, but oh well
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Role with name " + adminRole + " not found"));

        List<User> adminUsers = userRepository.findByRoles_Name(role.getName());
        if (adminUsers.isEmpty()) {
            // No users with admin rights, so create a new admin account
            User admin = new User("00000000000",
                    "ADMIN",
                    "USER",
                    "admin@localhost",
                    null,
                    passwordEncoder.encode(DEFAULT_PASSWORD));
            admin.getRoles().add(role);
            userRepository.save(admin);
            String name = admin.getFirstName() + " " + admin.getLastName();
            log.info("Created admin user '{}'", name);
        } else {
            // Found at least one admin
            log.info("Administrator account present");
        }
    }

    public String getDefaultAdminPassword() {
        return DEFAULT_PASSWORD;
    }
}
