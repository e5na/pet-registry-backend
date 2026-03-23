package com.petreg.prototype.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petreg.prototype.model.Breed;

public interface BreedRepository extends JpaRepository<Breed, Long> {

    List<Breed> findBySpecies_Id(Long speciesId);
}
