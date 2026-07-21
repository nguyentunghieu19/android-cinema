package com.cinema.backend.controller;

import com.cinema.backend.dto.request.CinemaRequest;
import com.cinema.backend.dto.response.CinemaResponse;
import com.cinema.backend.entity.Cinema;
import com.cinema.backend.mapper.CinemaMapper;
import com.cinema.backend.service.CinemaService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cinemas")
public class CinemaController {

    private final CinemaService cinemaService;
    private final CinemaMapper cinemaMapper;

    public CinemaController(CinemaService cinemaService,
                            CinemaMapper cinemaMapper) {
        this.cinemaService = cinemaService;
        this.cinemaMapper = cinemaMapper;
    }

    @GetMapping
    public List<CinemaResponse> getAll() {

        return cinemaService.getAllCinemas()
                .stream()
                .map(cinemaMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public CinemaResponse getById(@PathVariable Integer id) {

        Cinema cinema = cinemaService.getCinemaById(id)
                .orElseThrow(() -> new RuntimeException("Cinema not found"));

        return cinemaMapper.toResponse(cinema);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @PostMapping
    public CinemaResponse create(@RequestBody CinemaRequest request) {

        return cinemaMapper.toResponse(
                cinemaService.createCinema(request)
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @PutMapping("/{id}")
    public CinemaResponse update(@PathVariable Integer id,
                                 @RequestBody CinemaRequest request) {

        return cinemaMapper.toResponse(
                cinemaService.updateCinema(id, request)
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {

        cinemaService.deleteCinema(id);

        return "Delete cinema successfully.";
    }

}