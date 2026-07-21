package com.cinema.backend.mapper;

import com.cinema.backend.dto.request.MovieRequest;
import com.cinema.backend.dto.response.MovieResponse;
import com.cinema.backend.entity.Movie;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {

    public MovieResponse toResponse(Movie movie) {

        MovieResponse dto = new MovieResponse();

        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setDescription(movie.getDescription());
        dto.setDuration(movie.getDuration());
        dto.setGenre(movie.getGenre());
        dto.setDirector(movie.getDirector());
        dto.setActors(movie.getActors());
        dto.setLanguage(movie.getLanguage());
        dto.setRated(movie.getRated());
        dto.setReleaseDate(movie.getReleaseDate());
        dto.setPoster(movie.getPoster());
        dto.setTrailer(movie.getTrailer());
        dto.setStatus(movie.getStatus());

        return dto;
    }

    public Movie toEntity(MovieRequest request) {

        Movie movie = new Movie();
        updateEntity(movie, request);

        return movie;
    }

    public void updateEntity(Movie movie, MovieRequest request) {

        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setDuration(request.getDuration());
        movie.setGenre(request.getGenre());
        movie.setDirector(request.getDirector());
        movie.setActors(request.getActors());
        movie.setLanguage(request.getLanguage());
        movie.setRated(request.getRated());
        movie.setReleaseDate(request.getReleaseDate());
        movie.setPoster(request.getPoster());
        movie.setTrailer(request.getTrailer());
        movie.setStatus(request.getStatus());
    }

}