package com.cinema.backend.service;

import com.cinema.backend.dto.request.SeatRequest;
import com.cinema.backend.dto.response.SeatResponse;

import java.util.List;

public interface SeatService {

    List<SeatResponse> getAll();

    SeatResponse getById(Integer id);

    SeatResponse create(SeatRequest request);

    SeatResponse update(Integer id, SeatRequest request);

    void delete(Integer id);
}