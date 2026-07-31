package com.cinema.backend.repository;

import com.cinema.backend.entity.Combo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComboRepository extends JpaRepository<Combo, Integer> {

    List<Combo> findByStatusTrue();

}
