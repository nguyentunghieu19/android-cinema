package com.cinema.backend.mapper;

import com.cinema.backend.dto.request.CinemaRequest;
import com.cinema.backend.dto.response.CinemaResponse;
import com.cinema.backend.entity.Cinema;
import org.springframework.stereotype.Component;

@Component
public class CinemaMapper {

    public Cinema toEntity(CinemaRequest request) {

        Cinema cinema = new Cinema();
        updateEntity(cinema, request);

        return cinema;
    }

    public void updateEntity(Cinema cinema, CinemaRequest request) {

        cinema.setName(request.getName());
        cinema.setAddress(request.getAddress());
        cinema.setPhone(request.getPhone());
        cinema.setStatus(request.getStatus());
    }

    public CinemaResponse toResponse(Cinema cinema) {

        CinemaResponse response = new CinemaResponse();

        response.setId(cinema.getId());
        response.setName(cinema.getName());
        response.setAddress(cinema.getAddress());
        response.setPhone(cinema.getPhone());
        response.setStatus(cinema.getStatus());

        return response;
    }
}