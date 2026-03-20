package com.petreg.prototype.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petreg.prototype.model.User;
import com.petreg.prototype.model.type.RoleEnum;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPersonalCode(String personalCode);

    boolean existsByPersonalCode(String personalCode);

    List<User> findByRoles_Name(RoleEnum roleName);

}
