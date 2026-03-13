package com.petreg.prototype.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petreg.prototype.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPersonalCode(String personalCode);

    boolean existsByPersonalCode(String personalCode);
}
