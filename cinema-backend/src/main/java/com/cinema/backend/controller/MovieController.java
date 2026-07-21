package com.cinema.backend.controller;

import com.cinema.backend.dto.request.MovieRequest;
import com.cinema.backend.dto.response.MovieResponse;
import com.cinema.backend.entity.Movie;
import com.cinema.backend.mapper.MovieMapper;
import com.cinema.backend.service.MovieService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.cinema.backend.service.ShowtimeService;
import com.cinema.backend.dto.response.ShowtimeResponse;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;
    private final MovieMapper movieMapper;
    private final ShowtimeService showtimeService;
    public MovieController(MovieService movieService,
                           MovieMapper movieMapper,ShowtimeService showtimeService) {
        this.movieService = movieService;
        this.movieMapper = movieMapper;
        this.showtimeService=showtimeService;
    }

    @GetMapping
    public List<MovieResponse> getAllMovies() {

        return movieService.getAllMovies()
                .stream()
                .map(movieMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public MovieResponse getMovieById(@PathVariable Integer id) {

        Movie movie = movieService.getMovieById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        return movieMapper.toResponse(movie);
    }
    @GetMapping("/{id}/showtimes")
    public List<ShowtimeResponse> getShowtimes(@PathVariable Integer id) {
        return showtimeService.getByMovie(id);
    }
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @PostMapping
    public MovieResponse createMovie(@RequestBody MovieRequest request) {

        Movie movie = movieService.createMovie(request);

        return movieMapper.toResponse(movie);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @PutMapping("/{id}")
    public MovieResponse updateMovie(@PathVariable Integer id,
                                     @RequestBody MovieRequest request) {

        Movie movie = movieService.updateMovie(id, request);

        return movieMapper.toResponse(movie);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @DeleteMapping("/{id}")
    public String deleteMovie(@PathVariable Integer id) {

        movieService.deleteMovie(id);

        return "Delete movie successfully.";
    }

}