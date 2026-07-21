package com.cinema.backend.controller;

import com.cinema.backend.dto.request.SeatRequest;
import com.cinema.backend.dto.response.SeatResponse;
import com.cinema.backend.service.SeatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping
    public List<SeatResponse> getAll() {
        return seatService.getAll();
    }

    @GetMapping("/{id}")
    public SeatResponse getById(@PathVariable Integer id) {
        return seatService.getById(id);
    }

    @PostMapping
    public SeatResponse create(@RequestBody SeatRequest request) {
        return seatService.create(request);
    }

    @PutMapping("/{id}")
    public SeatResponse update(
            @PathVariable Integer id,
            @RequestBody SeatRequest request) {

        return seatService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        seatService.delete(id);
    }
}