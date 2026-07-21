package com.cinema.backend.service;

import com.cinema.backend.dto.request.CreateBookingRequest;
import com.cinema.backend.dto.response.CreateBookingResponse;
import com.cinema.backend.dto.response.MyBookingResponse;
import com.cinema.backend.dto.response.BookingDetailResponse;
import java.util.List;
public interface BookingService {

    CreateBookingResponse createBooking(
            CreateBookingRequest request,
            String username
    );
    List<MyBookingResponse> getMyBookings();
    BookingDetailResponse getBookingDetail(Integer bookingId);
    void cancelBooking(Integer bookingId);
    void expirePendingBookings();
}