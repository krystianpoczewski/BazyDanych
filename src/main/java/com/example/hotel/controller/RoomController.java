package com.example.hotel.controller;

import com.example.hotel.model.Room;
import com.example.hotel.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/rooms") // Base URL for room endpoints
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PostMapping
    public ResponseEntity<Room> addRoom(@RequestBody Room room) {
        Room savedRoom = roomService.saveRoom(room);
        if (savedRoom == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Room not saved, invalid input
        }
        return new ResponseEntity<>(savedRoom, HttpStatus.CREATED); // Return created room with 201 status
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoom(@PathVariable Integer id) {
        Room room = roomService.getRoomById(id);
        if (room == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Room not found
        }
        return new ResponseEntity<>(room, HttpStatus.OK); // Return room with 200 OK status
    }

    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms(); // Return list of all rooms
    }

    @GetMapping("/search")
    public List<Room> searchRooms(
            @RequestParam(required = false) Integer roomType,
            @RequestParam(required = false) Integer capacity,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Date startDate,
            @RequestParam(required = false) Date endDate) {

        return roomService.findAvailableRooms(roomType, capacity, maxPrice, startDate, endDate); // Call service for filtered rooms
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeRoom(@PathVariable Integer id) {
        Room removedRoom = roomService.removeRoomById(id);
        if (removedRoom == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Room not found
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Successfully removed, 204 status
    }
}
