package com.cinema.backend.service.impl;

import com.cinema.backend.dto.request.CinemaRequest;
import com.cinema.backend.entity.Cinema;
import com.cinema.backend.mapper.CinemaMapper;
import com.cinema.backend.repository.CinemaRepository;
import com.cinema.backend.service.CinemaService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CinemaServiceImpl implements CinemaService {

    private final CinemaRepository cinemaRepository;
    private final CinemaMapper cinemaMapper;

    public CinemaServiceImpl(CinemaRepository cinemaRepository,
                             CinemaMapper cinemaMapper) {
        this.cinemaRepository = cinemaRepository;
        this.cinemaMapper = cinemaMapper;
    }

    @Override
    public List<Cinema> getAllCinemas() {
        return cinemaRepository.findAll();
    }

    @Override
    public Optional<Cinema> getCinemaById(Integer id) {
        return cinemaRepository.findById(id);
    }

    @Override
    public Cinema createCinema(CinemaRequest request) {

        Cinema cinema = cinemaMapper.toEntity(request);

        return cinemaRepository.save(cinema);
    }

    @Override
    public Cinema updateCinema(Integer id, CinemaRequest request) {

        Cinema cinema = cinemaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cinema not found"));

        cinemaMapper.updateEntity(cinema, request);

        return cinemaRepository.save(cinema);
    }

    @Override
    public void deleteCinema(Integer id) {
        cinemaRepository.deleteById(id);
    }

}