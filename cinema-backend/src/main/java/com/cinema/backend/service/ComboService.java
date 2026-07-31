package com.cinema.backend.service;
import com.cinema.backend.dto.response.BookingComboResponse;
import com.cinema.backend.dto.response.ComboResponse;
import com.cinema.backend.dto.request.AddComboRequest;
import java.util.List;

public interface ComboService {

    List<ComboResponse> getAllCombos();
    void addComboToBooking(
            Integer bookingId,
            AddComboRequest request
    );
    List<BookingComboResponse> getBookingCombos(
            Integer bookingId
    );

    void removeComboFromBooking(
            Integer bookingId,
            Integer comboId
    );
}