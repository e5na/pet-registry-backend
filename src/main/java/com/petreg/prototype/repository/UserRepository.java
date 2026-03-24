package com.petreg.prototype.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.petreg.prototype.model.User;
import com.petreg.prototype.model.type.RoleEnum;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPersonalCode(String personalCode);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.personalCode = :personalCode")
    Optional<User> findByPersonalCodeWithRoles(@Param("personalCode") String personalCode);

    boolean existsByPersonalCode(String personalCode);

    List<User> findByRoles_Name(RoleEnum roleName);

}
