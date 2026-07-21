package com.cinema.backend.service;

import com.cinema.backend.dto.request.CinemaRequest;
import com.cinema.backend.entity.Cinema;

import java.util.List;
import java.util.Optional;

public interface CinemaService {

    List<Cinema> getAllCinemas();

    Optional<Cinema> getCinemaById(Integer id);

    Cinema createCinema(CinemaRequest request);

    Cinema updateCinema(Integer id, CinemaRequest request);

    void deleteCinema(Integer id);

}