package com.cinema.backend.service.impl;

import com.cinema.backend.repository.SeatRepository;
import com.cinema.backend.repository.TicketRepository;
import com.cinema.backend.dto.request.ShowtimeRequest;
import com.cinema.backend.dto.response.ShowtimeResponse;
import com.cinema.backend.entity.Movie;
import com.cinema.backend.entity.Room;
import com.cinema.backend.entity.Showtime;
import com.cinema.backend.mapper.ShowtimeMapper;
import com.cinema.backend.repository.MovieRepository;
import com.cinema.backend.repository.RoomRepository;
import com.cinema.backend.repository.ShowtimeRepository;
import com.cinema.backend.service.ShowtimeService;
import org.springframework.stereotype.Service;
import com.cinema.backend.dto.response.SeatMapResponse;
import com.cinema.backend.entity.Seat;
import com.cinema.backend.entity.Ticket;
import com.cinema.backend.enums.TicketStatus;

import java.util.ArrayList;
import java.util.List;


@Service
public class ShowtimeServiceImpl implements ShowtimeService {

    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final ShowtimeMapper showtimeMapper;
    private final SeatRepository seatRepository;
    private final TicketRepository ticketRepository;
    public ShowtimeServiceImpl(
            ShowtimeRepository showtimeRepository,
            MovieRepository movieRepository,
            RoomRepository roomRepository,
            ShowtimeMapper showtimeMapper,
            SeatRepository seatRepository,
            TicketRepository ticketRepository) {

        this.showtimeRepository = showtimeRepository;
        this.movieRepository = movieRepository;
        this.roomRepository = roomRepository;
        this.showtimeMapper = showtimeMapper;

        this.seatRepository = seatRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public List<ShowtimeResponse> getAll() {

        return showtimeRepository.findAll()
                .stream()
                .map(showtimeMapper::toResponse)
                .toList();

    }

    @Override
    public ShowtimeResponse getById(Integer id) {

        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Showtime not found"));

        return showtimeMapper.toResponse(showtime);

    }
    @Override
    public List<ShowtimeResponse> getByMovie(Integer movieId) {

        return showtimeRepository.findByMovieId(movieId)
                .stream()
                .map(showtimeMapper::toResponse)
                .toList();
    }
    @Override
    public ShowtimeResponse create(ShowtimeRequest request) {

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        Showtime showtime = new Showtime();

        showtime.setMovie(movie);
        showtime.setRoom(room);
        showtime.setStartTime(request.getStartTime());
        showtime.setEndTime(request.getEndTime());
        showtime.setPrice(request.getPrice());
        showtime.setStatus(request.getStatus());

        return showtimeMapper.toResponse(
                showtimeRepository.save(showtime)
        );
    }

    @Override
    public ShowtimeResponse update(Integer id, ShowtimeRequest request) {

        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Showtime not found"));

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        showtime.setMovie(movie);
        showtime.setRoom(room);
        showtime.setStartTime(request.getStartTime());
        showtime.setEndTime(request.getEndTime());
        showtime.setPrice(request.getPrice());
        showtime.setStatus(request.getStatus());

        return showtimeMapper.toResponse(
                showtimeRepository.save(showtime)
        );
    }

    @Override
    public void delete(Integer id) {

        showtimeRepository.deleteById(id);

    }
    @Override
    public List<SeatMapResponse> getSeatMap(Integer showtimeId) {

        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy suất chiếu"));

        List<Seat> seats =
                seatRepository.findByRoomId(showtime.getRoom().getId());

        List<SeatMapResponse> result = new ArrayList<>();

        for (Seat seat : seats) {

            SeatMapResponse response = new SeatMapResponse();

            response.setSeatId(seat.getId());
            response.setSeatRow(seat.getSeatRow());
            response.setSeatNumber(seat.getSeatNumber());

            response.setStatus("AVAILABLE");

            List<Ticket> tickets =
                    ticketRepository.findByShowtimeId(showtimeId);



            for (Ticket ticket : tickets) {

                switch (ticket.getStatus()) {

                    case PENDING:
                        response.setStatus("HOLDING");
                        break;

                    case BOOKED:
                        response.setStatus("BOOKED");
                        break;

                    default:
                        break;
                }

                if (!response.getStatus().equals("AVAILABLE")) {
                    break;
                }
            }

            result.add(response);
        }

        return result;

    }
}