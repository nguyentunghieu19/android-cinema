package com.cinema.backend.repository;

import com.cinema.backend.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cinema.backend.enums.TicketStatus;
import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    Optional<Ticket> findByTicketCode(String ticketCode);

    List<Ticket> findByBookingId(Integer bookingId);
    List<Ticket> findByShowtimeId(Integer showtimeId);
    List<Ticket> findByShowtimeIdAndSeatId(
            Integer showtimeId,
            Integer seatId
    );
    boolean existsBySeatIdAndBooking_Showtime_Id(
            Integer seatId,
            Integer showtimeId
    );
    boolean existsByShowtimeIdAndSeatIdAndStatus(
            Integer showtimeId,
            Integer seatId,
            TicketStatus status
    );




}