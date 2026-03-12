package com.petreg.prototype.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petreg.prototype.model.Pet;

public interface PetRepository extends JpaRepository<Pet, Long> {}
