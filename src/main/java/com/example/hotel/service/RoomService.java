package com.example.hotel.service;

import com.example.hotel.model.Room;
import com.example.hotel.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public List<Room> findAvailableRooms(Integer roomType, Integer capacity, BigDecimal maxPrice, Date startDate, Date endDate) {
        return roomRepository.findAvailableRoomsByFilters(roomType, capacity, maxPrice, startDate, endDate);
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(Integer roomId) {
        return roomRepository.findById(roomId).orElse(null);
    }

    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }

    public Room removeRoomById(Integer roomId) {
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room != null) {
            roomRepository.delete(room);
        }
        return room;
    }
}
