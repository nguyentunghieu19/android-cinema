package com.cinema.backend.controller;

import com.cinema.backend.dto.request.ShowtimeRequest;
import com.cinema.backend.dto.response.ShowtimeResponse;
import com.cinema.backend.service.ShowtimeService;
import org.springframework.web.bind.annotation.*;
import com.cinema.backend.dto.response.SeatMapResponse;
import java.util.List;

@RestController
@RequestMapping("/api/showtimes")
public class ShowtimeController {

    private final ShowtimeService showtimeService;

    public ShowtimeController(ShowtimeService showtimeService) {
        this.showtimeService = showtimeService;
    }

    @GetMapping
    public List<ShowtimeResponse> getAll() {
        return showtimeService.getAll();
    }

    @GetMapping("/{id}/seats")
    public List<SeatMapResponse> getSeatMap(@PathVariable Integer id) {
        return showtimeService.getSeatMap(id);
    }
    @GetMapping("/{id}")
    public ShowtimeResponse getById(@PathVariable Integer id) {
        return showtimeService.getById(id);
    }

    @PostMapping
    public ShowtimeResponse create(@RequestBody ShowtimeRequest request) {
        return showtimeService.create(request);
    }

    @PutMapping("/{id}")
    public ShowtimeResponse update(@PathVariable Integer id,
                                   @RequestBody ShowtimeRequest request) {
        return showtimeService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        showtimeService.delete(id);
    }
}