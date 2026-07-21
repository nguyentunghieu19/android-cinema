package com.cinema.backend.repository;

import com.cinema.backend.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import com.cinema.backend.enums.BookingStatus;
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Optional<Booking> findByBookingCode(String bookingCode);
    List<Booking> findByUserUsernameOrderByBookingTimeDesc(String username);
    List<Booking> findByStatus(BookingStatus status);
}