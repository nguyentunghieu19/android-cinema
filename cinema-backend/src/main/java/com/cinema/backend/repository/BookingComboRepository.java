package com.cinema.backend.repository;
import java.util.Optional;
import com.cinema.backend.entity.BookingCombo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingComboRepository extends JpaRepository<BookingCombo, Integer> {

    List<BookingCombo> findByBookingId(Integer bookingId);
    Optional<BookingCombo> findByBookingIdAndComboId(
            Integer bookingId,
            Integer comboId
    );

}