package com.cinema.backend.mapper;

import com.cinema.backend.dto.response.ShowtimeResponse;
import com.cinema.backend.entity.Showtime;
import org.springframework.stereotype.Component;

@Component
public class ShowtimeMapper {

    public ShowtimeResponse toResponse(Showtime showtime){

        ShowtimeResponse response = new ShowtimeResponse();

        response.setId(showtime.getId());

        response.setMovieId(showtime.getMovie().getId());
        response.setMovieTitle(showtime.getMovie().getTitle());

        response.setRoomId(showtime.getRoom().getId());
        response.setRoomName(showtime.getRoom().getName());

        response.setCinemaId(showtime.getRoom().getCinema().getId());
        response.setCinemaName(showtime.getRoom().getCinema().getName());

        response.setStartTime(showtime.getStartTime());
        response.setEndTime(showtime.getEndTime());

        response.setPrice(showtime.getPrice());

        response.setStatus(showtime.getStatus());

        return response;
    }

}