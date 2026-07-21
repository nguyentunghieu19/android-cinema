package com.cinema.backend.service;

import com.cinema.backend.dto.request.ShowtimeRequest;
import com.cinema.backend.dto.response.ShowtimeResponse;
import com.cinema.backend.dto.response.SeatMapResponse;
import java.util.List;

public interface ShowtimeService {

    List<ShowtimeResponse> getAll();

    ShowtimeResponse getById(Integer id);

    ShowtimeResponse create(ShowtimeRequest request);

    ShowtimeResponse update(Integer id, ShowtimeRequest request);

    List<SeatMapResponse> getSeatMap(Integer showtimeId);
    void delete(Integer id);
    
    List<ShowtimeResponse> getByMovie(Integer movieId);

}