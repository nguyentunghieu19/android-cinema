package com.cinema.backend.repository;

import com.cinema.backend.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ShowtimeRepository extends JpaRepository<Showtime, Integer> {
    List<Showtime> findByMovieId(Integer movieId);
}