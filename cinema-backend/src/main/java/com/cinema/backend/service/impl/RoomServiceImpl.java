package com.cinema.backend.service.impl;

import com.cinema.backend.dto.request.RoomRequest;
import com.cinema.backend.dto.response.RoomResponse;
import com.cinema.backend.entity.Cinema;
import com.cinema.backend.entity.Room;
import com.cinema.backend.mapper.RoomMapper;
import com.cinema.backend.repository.CinemaRepository;
import com.cinema.backend.repository.RoomRepository;
import com.cinema.backend.service.RoomService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final CinemaRepository cinemaRepository;
    private final RoomMapper roomMapper;

    public RoomServiceImpl(RoomRepository roomRepository,
                           CinemaRepository cinemaRepository,
                           RoomMapper roomMapper) {

        this.roomRepository = roomRepository;
        this.cinemaRepository = cinemaRepository;
        this.roomMapper = roomMapper;
    }

    @Override
    public List<RoomResponse> getAll() {

        return roomRepository.findAll()
                .stream()
                .map(roomMapper::toResponse)
                .toList();

    }

    @Override
    public RoomResponse getById(Integer id) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        return roomMapper.toResponse(room);

    }

    @Override
    public RoomResponse create(RoomRequest request) {

        Cinema cinema = cinemaRepository.findById(request.getCinemaId())
                .orElseThrow(() -> new RuntimeException("Cinema not found"));

        Room room = roomMapper.toEntity(request, cinema);

        roomRepository.save(room);

        return roomMapper.toResponse(room);

    }

    @Override
    public RoomResponse update(Integer id, RoomRequest request) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        Cinema cinema = cinemaRepository.findById(request.getCinemaId())
                .orElseThrow(() -> new RuntimeException("Cinema not found"));

        roomMapper.update(room, request, cinema);

        roomRepository.save(room);

        return roomMapper.toResponse(room);

    }

    @Override
    public void delete(Integer id) {

        roomRepository.deleteById(id);

    }

}