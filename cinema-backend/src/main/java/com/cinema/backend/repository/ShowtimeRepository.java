package com.cinema.backend.repository;

import com.cinema.backend.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime, Integer> {

    List<Showtime> findByMovieId(Integer movieId);

    @Query("SELECT s FROM Showtime s " +
            "WHERE s.room.id = :roomId " +
            "AND s.startTime < :endTime " +
            "AND s.endTime > :startTime " +
            "AND (:excludeId IS NULL OR s.id <> :excludeId)")
    List<Showtime> findOverlapping(
            @Param("roomId") Integer roomId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("excludeId") Integer excludeId
    );
}