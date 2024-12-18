package com.example.hotel_bd.controllers;

import com.example.hotel_bd.models.RoomType;
import com.example.hotel_bd.repository.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Rest Controller for managing RoomType entities.
 * Provides CRUD operations for RoomTypes in the system.
 */
@RestController
@RequestMapping("/api")
public class RoomTypeController {

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    /**
     * Adds a new RoomType to the repository if it does not already exist.
     * This method is restricted to users with the 'ADMIN' role.
     *
     * @param requestRoomType the RoomType object containing the details to be added
     * @return a ResponseEntity containing the newly added RoomType, or a bad request response if the
     *         RoomType already exists
     */
    @PostMapping("/admin/room-type")
    public ResponseEntity<RoomType> addRoomType(@RequestBody RoomType requestRoomType) {
        Optional<RoomType> existingRoomType = roomTypeRepository.findByName(requestRoomType.getName());
        if (existingRoomType.isPresent()) {
            return ResponseEntity.badRequest().body(existingRoomType.get());
        }
        roomTypeRepository.save(requestRoomType);
        return ResponseEntity.ok(requestRoomType);
    }

    /**
     * Updates an existing RoomType entity with new details provided in the request.
     * This method is restricted to users with the 'ADMIN' role.
     *
     * @param requestRoomType the RoomType object containing updated information
     * @return a ResponseEntity containing the updated RoomType if successful, or a not found response
     *         if the RoomType does not exist
     */
    @PutMapping("/admin/room-type/{id}")
    public ResponseEntity<RoomType> updateRoomType(Integer id, @RequestBody RoomType requestRoomType) {
        Optional<RoomType> existingRoomTypeOpt = roomTypeRepository.findById(id);
        if (existingRoomTypeOpt.isPresent()) {
            RoomType existingRoomType = existingRoomTypeOpt.get();
            existingRoomType.setName(requestRoomType.getName());
            roomTypeRepository.save(existingRoomType);
            return ResponseEntity.ok(existingRoomType);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Deletes a RoomType entity identified by the provided ID.
     * This method is restricted to users with the 'ADMIN' role.
     *
     * @param id the ID of the RoomType to be deleted
     * @return a ResponseEntity with a success message if the RoomType is found and deleted,
     *         or a not found response if the RoomType does not exist
     */
    @DeleteMapping("/admin/room-type/{id}")
    public ResponseEntity<String> deleteRoomType(@PathVariable Integer id) {
        Optional<RoomType> roomTypeOpt = roomTypeRepository.findById(id);
        if (roomTypeOpt.isPresent()) {
            roomTypeRepository.delete(roomTypeOpt.get());
            return ResponseEntity.ok("Room type deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Retrieves a list of all available RoomType entities from the repository.
     *
     * @return a ResponseEntity containing a list of RoomType instances
     */
    @GetMapping("/user/room-type")
    public ResponseEntity<List<RoomType>> getAllRoomTypes() {
        List<RoomType> roomTypes = roomTypeRepository.findAll();
        return ResponseEntity.ok(roomTypes);
    }

    /**
     * Retrieves a RoomType entity based on the provided ID.
     *
     * @param id the ID of the RoomType to retrieve
     * @return a ResponseEntity containing the RoomType if found, or a not found response if the RoomType does not exist
     */
    @GetMapping("/user/room-type/{id}")
    public ResponseEntity<RoomType> getRoomTypeById(@PathVariable Integer id) {
        Optional<RoomType> roomTypeOpt = roomTypeRepository.findById(id);
        if (roomTypeOpt.isPresent()) {
            return ResponseEntity.ok(roomTypeOpt.get());
        }
        return ResponseEntity.notFound().build();
    }
}

