package com.example.hotel_bd.controllers;

import com.example.hotel_bd.models.RoomAmenities;
import com.example.hotel_bd.repository.RoomAmenitiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/room/amenities")
public class RoomAmenitiesController {
    @Autowired
    private RoomAmenitiesRepository roomAmenitiesRepository;

    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomAmenities> addRoomAmenity(@RequestBody RoomAmenities requestRoomAmenity) {
        Optional<RoomAmenities> existingRoomAmenity = roomAmenitiesRepository.findByName(requestRoomAmenity.getName());
        if (existingRoomAmenity.isPresent()) {
            return ResponseEntity.badRequest().body(existingRoomAmenity.get());
        }
        roomAmenitiesRepository.save(requestRoomAmenity);
        return ResponseEntity.ok(requestRoomAmenity);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomAmenities> updateRoomType(@RequestBody RoomAmenities requestRoomAmenity) {
        Optional<RoomAmenities> existingRoomAmenity = roomAmenitiesRepository.findById(requestRoomAmenity.getId());
        if (existingRoomAmenity.isPresent()) {
            RoomAmenities existingRoomType = existingRoomAmenity.get();
            existingRoomType.setName(requestRoomAmenity.getName());
            roomAmenitiesRepository.save(existingRoomType);
            return ResponseEntity.ok(existingRoomType);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteRoomType(@PathVariable Integer id) {
        Optional<RoomAmenities> existingRoomAmenity = roomAmenitiesRepository.findById(id);
        if (existingRoomAmenity.isPresent()) {
            roomAmenitiesRepository.delete(existingRoomAmenity.get());
            return ResponseEntity.ok("Room amenity deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/")
    public ResponseEntity<List<RoomAmenities>> getAllRoomTypes() {
        List<RoomAmenities> roomAmenities = roomAmenitiesRepository.findAll();
        return ResponseEntity.ok(roomAmenities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomAmenities> getRoomType(@PathVariable Integer id) {
        Optional<RoomAmenities> existingRoomAmenity = roomAmenitiesRepository.findById(id);
        if (existingRoomAmenity.isPresent()) {
            return ResponseEntity.ok(existingRoomAmenity.get());
        }
        return ResponseEntity.notFound().build();
    }
}
