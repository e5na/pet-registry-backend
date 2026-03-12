package com.petreg.prototype.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petreg.prototype.model.Breed;

public interface BreedRepository extends JpaRepository<Breed, Long> {}
