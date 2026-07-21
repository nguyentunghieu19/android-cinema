package com.cinema.backend.controller;

import com.cinema.backend.dto.request.CreateBookingRequest;
import com.cinema.backend.dto.response.CreateBookingResponse;
import com.cinema.backend.service.BookingService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.cinema.backend.dto.response.MyBookingResponse;
import com.cinema.backend.dto.response.BookingDetailResponse;
import java.util.List;
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public CreateBookingResponse createBooking(
            @RequestBody CreateBookingRequest request,
            Authentication authentication
    ) {

        return bookingService.createBooking(
                request,
                authentication.getName()
        );

    }
    @GetMapping("/my")
    public List<MyBookingResponse> getMyBookings() {
        return bookingService.getMyBookings();
    }
    @GetMapping("/{id}")
    public BookingDetailResponse getBookingDetail(
            @PathVariable Integer id) {

        return bookingService.getBookingDetail(id);
    }
    @PutMapping("/{id}/cancel")
    public String cancelBooking(@PathVariable Integer id) {

        bookingService.cancelBooking(id);

        return "Booking cancelled successfully";
    }

}