package com.cinema.backend.scheduler;

import com.cinema.backend.service.BookingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BookingScheduler {

    private final BookingService bookingService;

    public BookingScheduler(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Scheduled(fixedRate = 60000)
    public void expireBookings() {

        bookingService.expirePendingBookings();

    }

}