package com.cinema.backend.mapper;

import com.cinema.backend.dto.response.SeatResponse;
import com.cinema.backend.entity.Seat;
import org.springframework.stereotype.Component;

@Component
public class SeatMapper {

    public SeatResponse toResponse(Seat seat) {

        SeatResponse response = new SeatResponse();

        response.setId(seat.getId());
        response.setSeatRow(seat.getSeatRow());
        response.setSeatNumber(seat.getSeatNumber());
        response.setSeatType(seat.getSeatType());
        response.setStatus(seat.getStatus());

        response.setRoomId(seat.getRoom().getId());
        response.setRoomName(seat.getRoom().getName());

        return response;
    }
}