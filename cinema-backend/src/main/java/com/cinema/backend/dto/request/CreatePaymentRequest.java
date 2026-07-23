package com.cinema.backend.dto.request;

public class CreatePaymentRequest {

    private Integer bookingId;

    public CreatePaymentRequest() {
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }
}