package com.cinema.backend.controller;

import com.cinema.backend.dto.request.RoomRequest;
import com.cinema.backend.dto.response.RoomResponse;
import com.cinema.backend.service.RoomService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public List<RoomResponse> getAll() {
        return roomService.getAll();
    }

    @GetMapping("/{id}")
    public RoomResponse getById(@PathVariable Integer id) {
        return roomService.getById(id);
    }

    @PostMapping
    public RoomResponse create(@RequestBody RoomRequest request) {
        return roomService.create(request);
    }

    @PutMapping("/{id}")
    public RoomResponse update(@PathVariable Integer id,
                               @RequestBody RoomRequest request) {
        return roomService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        roomService.delete(id);
    }
}