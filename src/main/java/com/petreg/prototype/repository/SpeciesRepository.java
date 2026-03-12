package com.petreg.prototype.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petreg.prototype.model.Species;

public interface SpeciesRepository extends JpaRepository<Species, Long> {}
