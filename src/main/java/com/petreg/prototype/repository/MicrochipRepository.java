package com.petreg.prototype.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petreg.prototype.model.Microchip;

public interface MicrochipRepository extends JpaRepository<Microchip, Long> {

    public List<Microchip> findByMicrochipNumberContainingIgnoreCase(String microchipNumber);

}
