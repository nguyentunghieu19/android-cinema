package com.cinema.backend.service.impl;

import com.cinema.backend.dto.request.SeatRequest;
import com.cinema.backend.dto.response.SeatResponse;
import com.cinema.backend.entity.Room;
import com.cinema.backend.entity.Seat;
import com.cinema.backend.mapper.SeatMapper;
import com.cinema.backend.repository.RoomRepository;
import com.cinema.backend.repository.SeatRepository;
import com.cinema.backend.service.SeatService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final RoomRepository roomRepository;
    private final SeatMapper seatMapper;

    public SeatServiceImpl(
            SeatRepository seatRepository,
            RoomRepository roomRepository,
            SeatMapper seatMapper) {

        this.seatRepository = seatRepository;
        this.roomRepository = roomRepository;
        this.seatMapper = seatMapper;
    }

    @Override
    public List<SeatResponse> getAll() {
        return seatRepository.findAll()
                .stream()
                .map(seatMapper::toResponse)
                .toList();
    }

    @Override
    public SeatResponse getById(Integer id) {

        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        return seatMapper.toResponse(seat);
    }

    @Override
    public SeatResponse create(SeatRequest request) {

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        Seat seat = new Seat();

        seat.setSeatRow(request.getSeatRow());
        seat.setSeatNumber(request.getSeatNumber());
        seat.setSeatType(request.getSeatType());
        seat.setStatus(request.getStatus());
        seat.setRoom(room);

        return seatMapper.toResponse(
                seatRepository.save(seat)
        );
    }

    @Override
    public SeatResponse update(Integer id, SeatRequest request) {

        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        seat.setSeatRow(request.getSeatRow());
        seat.setSeatNumber(request.getSeatNumber());
        seat.setSeatType(request.getSeatType());
        seat.setStatus(request.getStatus());
        seat.setRoom(room);

        return seatMapper.toResponse(
                seatRepository.save(seat)
        );
    }

    @Override
    public void delete(Integer id) {

        seatRepository.deleteById(id);

    }
}