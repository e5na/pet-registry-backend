package com.petreg.prototype.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petreg.prototype.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
