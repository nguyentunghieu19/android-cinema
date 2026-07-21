package com.cinema.backend.mapper;

import com.cinema.backend.dto.request.RoomRequest;
import com.cinema.backend.dto.response.RoomResponse;
import com.cinema.backend.entity.Cinema;
import com.cinema.backend.entity.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {

    public Room toEntity(RoomRequest request, Cinema cinema) {

        Room room = new Room();

        room.setName(request.getName());
        room.setCapacity(request.getCapacity());
        room.setRoomType(request.getRoomType());
        room.setStatus(request.getStatus());
        room.setCinema(cinema);

        return room;
    }

    public RoomResponse toResponse(Room room) {

        RoomResponse response = new RoomResponse();

        response.setId(room.getId());
        response.setName(room.getName());
        response.setCapacity(room.getCapacity());
        response.setRoomType(room.getRoomType());
        response.setStatus(room.getStatus());

        response.setCinemaId(room.getCinema().getId());
        response.setCinemaName(room.getCinema().getName());

        return response;
    }

    public void update(Room room, RoomRequest request, Cinema cinema) {

        room.setName(request.getName());
        room.setCapacity(request.getCapacity());
        room.setRoomType(request.getRoomType());
        room.setStatus(request.getStatus());
        room.setCinema(cinema);

    }
}