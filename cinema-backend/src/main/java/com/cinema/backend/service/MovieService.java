package com.cinema.backend.service;

import com.cinema.backend.dto.request.MovieRequest;
import com.cinema.backend.entity.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieService {

    List<Movie> getAllMovies();

    Optional<Movie> getMovieById(Integer id);

    Movie createMovie(MovieRequest request);

    Movie updateMovie(Integer id, MovieRequest request);

    void deleteMovie(Integer id);

}