package com.cinema.backend.service.impl;

import com.cinema.backend.dto.request.MovieRequest;
import com.cinema.backend.entity.Movie;
import com.cinema.backend.mapper.MovieMapper;
import com.cinema.backend.repository.MovieRepository;
import com.cinema.backend.service.MovieService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public MovieServiceImpl(MovieRepository movieRepository,
                            MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public Optional<Movie> getMovieById(Integer id) {
        return movieRepository.findById(id);
    }

    @Override
    public Movie createMovie(MovieRequest request) {

        Movie movie = movieMapper.toEntity(request);

        return movieRepository.save(movie);
    }

    @Override
    public Movie updateMovie(Integer id, MovieRequest request) {

        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        movieMapper.updateEntity(movie, request);

        return movieRepository.save(movie);
    }

    @Override
    public void deleteMovie(Integer id) {
        movieRepository.deleteById(id);
    }

}