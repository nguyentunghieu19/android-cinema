package com.cinema.backend.service;

import com.cinema.backend.dto.request.RoomRequest;
import com.cinema.backend.dto.response.RoomResponse;

import java.util.List;

public interface RoomService {

    List<RoomResponse> getAll();

    RoomResponse getById(Integer id);

    RoomResponse create(RoomRequest request);

    RoomResponse update(Integer id, RoomRequest request);

    void delete(Integer id);

}