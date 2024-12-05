package com.example.hotel_bd.controllers;

import com.example.hotel_bd.dto.RoomDTO;
import com.example.hotel_bd.models.Room;
import com.example.hotel_bd.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/room")
public class RoomController {
    @Autowired
    RoomRepository roomRepo;

    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Room> addRoom(@RequestBody RoomDTO roomDTO) {
        Room room = new Room();
        room.setCapacity(roomDTO.getCapacity());
        room.setAmenities(roomDTO.getAmenities());
        room.setType(roomDTO.getType());
        room.setPricePerNight(roomDTO.getPricePerNight());

        roomRepo.save(room);
        return ResponseEntity.ok(room);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> removeRoom(@PathVariable Integer id) {
        Optional<Room> existingRoomOpt = roomRepo.findById(id);
        if (existingRoomOpt.isPresent()) {
            roomRepo.deleteById(id);
            return ResponseEntity.ok("Room deleted successfully.");
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Room> updateRoom(@PathVariable Integer id, @RequestBody RoomDTO roomDTO) {
        Optional<Room> existingRoomOpt = roomRepo.findById(id);
        if (existingRoomOpt.isPresent()) {
            Room existingRoom = existingRoomOpt.get();
            existingRoom.setCapacity(roomDTO.getCapacity());
            existingRoom.setAmenities(roomDTO.getAmenities());
            existingRoom.setType(roomDTO.getType());
            existingRoom.setPricePerNight(roomDTO.getPricePerNight());

            roomRepo.save(existingRoom);
            return ResponseEntity.ok(existingRoom);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/")
    public ResponseEntity<List<Room>> getAllRooms() {
        List<Room> rooms = roomRepo.findAll();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Integer id) {
        Optional<Room> existingRoomOpt = roomRepo.findById(id);
        if (existingRoomOpt.isPresent()) {
            Room existingRoom = existingRoomOpt.get();
            return ResponseEntity.ok(existingRoom);
        }
        return ResponseEntity.notFound().build();
    }
}
