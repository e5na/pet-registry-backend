package com.petreg.prototype.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petreg.prototype.model.Role;
import com.petreg.prototype.model.type.RoleEnum;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleEnum name);
}
