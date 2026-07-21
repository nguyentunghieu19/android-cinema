package com.cinema.backend.repository;

import com.cinema.backend.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface SeatRepository extends JpaRepository<Seat, Integer> {
    List<Seat> findAllByIdIn(List<Integer> ids);
    List<Seat> findByRoomId(Integer roomId);
}