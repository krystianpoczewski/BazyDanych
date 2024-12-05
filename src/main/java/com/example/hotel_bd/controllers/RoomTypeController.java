package com.example.hotel_bd.controllers;

import com.example.hotel_bd.models.RoomType;
import com.example.hotel_bd.repository.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/room/type")
public class RoomTypeController {

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomType> addRoomType(@RequestBody RoomType requestRoomType) {
        Optional<RoomType> existingRoomType = roomTypeRepository.findByName(requestRoomType.getName());
        if (existingRoomType.isPresent()) {
            return ResponseEntity.badRequest().body(existingRoomType.get());
        }
        roomTypeRepository.save(requestRoomType);
        return ResponseEntity.ok(requestRoomType);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomType> updateRoomType(@RequestBody RoomType requestRoomType) {
        Optional<RoomType> existingRoomTypeOpt = roomTypeRepository.findById(requestRoomType.getId());
        if (existingRoomTypeOpt.isPresent()) {
            RoomType existingRoomType = existingRoomTypeOpt.get();
            existingRoomType.setName(requestRoomType.getName());
            roomTypeRepository.save(existingRoomType);
            return ResponseEntity.ok(existingRoomType);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteRoomType(@PathVariable Integer id) {
        Optional<RoomType> roomTypeOpt = roomTypeRepository.findById(id);
        if (roomTypeOpt.isPresent()) {
            roomTypeRepository.delete(roomTypeOpt.get());
            return ResponseEntity.ok("Room type deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/")
    public ResponseEntity<List<RoomType>> getAllRoomTypes() {
        List<RoomType> roomTypes = roomTypeRepository.findAll();
        return ResponseEntity.ok(roomTypes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomType> getRoomTypeById(@PathVariable Integer id) {
        Optional<RoomType> roomTypeOpt = roomTypeRepository.findById(id);
        if (roomTypeOpt.isPresent()) {
            return ResponseEntity.ok(roomTypeOpt.get());
        }
        return ResponseEntity.notFound().build();
    }
}

