package com.petreg.prototype.bootstrap;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.petreg.prototype.model.Role;
import com.petreg.prototype.model.type.RoleEnum;
import com.petreg.prototype.repository.RoleRepository;

@Component
public class RoleSeeder {

    private static final Logger log = LoggerFactory.getLogger(RoleSeeder.class);

    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @EventListener(ContextRefreshedEvent.class)
    @Order(1)
    public void run() {
        createRoles();
    }

    public void createRoles() {
        log.info("Bootstrapping roles...");

        for (RoleEnum roleEnum : RoleEnum.values()) {
            Optional<Role> maybeRole = roleRepository.findByName(roleEnum);
            maybeRole.ifPresentOrElse(role -> log.info("Found {}", role.getName()), () -> {
                Role created = new Role();
                created.setName(roleEnum);
                roleRepository.save(created);
                log.info("Created {}", created.getName());
            });
        }
    }
}
