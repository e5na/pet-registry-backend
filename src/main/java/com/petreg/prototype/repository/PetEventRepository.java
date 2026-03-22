package com.petreg.prototype.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petreg.prototype.model.PetEvent;

public interface PetEventRepository extends JpaRepository<PetEvent, Long> {

    List<PetEvent> findByPet_IdOrderByTimestampDesc(Long petId);
}
