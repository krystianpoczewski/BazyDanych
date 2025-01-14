package com.example.hotel_bd.controllers;

import com.example.hotel_bd.dto.NameRequest;
import com.example.hotel_bd.models.RoomAmenities;
import com.example.hotel_bd.repository.RoomAmenitiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * RoomAmenitiesController is a REST controller that manages room amenities in a hotel application.
 * It provides endpoints to create, update, delete, and retrieve room amenities.
 * Only users with the ADMIN role are authorized to add, update, or delete room amenities.
 */
@RestController
@RequestMapping("/api")
public class RoomAmenitiesController {
    @Autowired
    private RoomAmenitiesRepository roomAmenitiesRepository;

    /**
     * Adds a new room amenity to the repository. If an amenity with the same name already exists,
     * it will return a bad request response with the existing amenity.
     *
     * @param requestRoomAmenity the room amenity to be added
     * @return a ResponseEntity containing the added room amenity, or the existing one if a name conflict arises
     */
    @PostMapping("/admin/room-amenities")
    public ResponseEntity<RoomAmenities> addRoomAmenity(@RequestBody RoomAmenities requestRoomAmenity) {
        Optional<RoomAmenities> existingRoomAmenity = roomAmenitiesRepository.findByName(requestRoomAmenity.getName());
        if (existingRoomAmenity.isPresent()) {
            return ResponseEntity.badRequest().body(existingRoomAmenity.get());
        }
        roomAmenitiesRepository.save(requestRoomAmenity);
        return ResponseEntity.ok(requestRoomAmenity);
    }

    /**
     * Updates the details of an existing room amenity based on the provided information.
     * If the amenity with the given ID exists, its name is updated and the updated
     * entity is returned. If the amenity does not exist, a not found response is returned.
     *
     * @param requestRoomAmenity the RoomAmenities object containing the updated details including its ID
     * @return a ResponseEntity containing the updated RoomAmenities object if the update is successful,
     *         or a ResponseEntity with a not found status if the room amenity does not exist
     */
    @PutMapping("/admin/room-amenities/{id}")
    public ResponseEntity<RoomAmenities> updateRoomType(@PathVariable Integer id, @RequestBody NameRequest requestName) {
        Optional<RoomAmenities> existingRoomAmenity = roomAmenitiesRepository.findById(id);
        if (existingRoomAmenity.isPresent()) {
            RoomAmenities existingRoomType = existingRoomAmenity.get();
            existingRoomType.setName(requestName.getName());
            roomAmenitiesRepository.save(existingRoomType);
            return ResponseEntity.ok(existingRoomType);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Deletes a room amenity identified by its unique ID.
     * Only accessible by users with the ADMIN role.
     *
     * @param id the unique identifier of the room amenity to be deleted
     * @return ResponseEntity with a success message if the room amenity is found and deleted,
     *         or a ResponseEntity with a 404 status if the room amenity is not found
     */
    @DeleteMapping("/admin/room-amenities/{id}")
    public ResponseEntity<String> deleteRoomType(@PathVariable Integer id) {
        Optional<RoomAmenities> existingRoomAmenity = roomAmenitiesRepository.findById(id);
        if (existingRoomAmenity.isPresent()) {
            roomAmenitiesRepository.delete(existingRoomAmenity.get());
            return ResponseEntity.ok("Room amenity deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Retrieves a list of all room amenities.
     *
     * @return a ResponseEntity containing a list of RoomAmenities objects
     */
    @GetMapping("/user/room-amenities")
    public ResponseEntity<List<RoomAmenities>> getAllRoomTypes() {
        List<RoomAmenities> roomAmenities = roomAmenitiesRepository.findAll();
        return ResponseEntity.ok(roomAmenities);
    }

    /**
     * Handles HTTP GET requests for retrieving room amenities by ID.
     *
     * @param id the ID of the room amenity to retrieve
     * @return ResponseEntity containing the requested RoomAmenities if found,
     *         or a ResponseEntity with HTTP status 404 (Not Found) if the ID does not exist
     */
    @GetMapping("/user/room-amenities/{id}")
    public ResponseEntity<RoomAmenities> getRoomAmenity(@PathVariable Integer id) {
        Optional<RoomAmenities> existingRoomAmenity = roomAmenitiesRepository.findById(id);
        if (existingRoomAmenity.isPresent()) {
            return ResponseEntity.ok(existingRoomAmenity.get());
        }
        return ResponseEntity.notFound().build();
    }
}
