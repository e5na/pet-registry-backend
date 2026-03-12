package com.petreg.prototype.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petreg.prototype.model.Microchip;

public interface MicrochipRepository extends JpaRepository<Microchip, Long> {
}
