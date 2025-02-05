package com.example.hotel_bd.controllers;

import com.example.hotel_bd.dto.RoomDTO;
import com.example.hotel_bd.models.Reservation;
import com.example.hotel_bd.models.Room;
import com.example.hotel_bd.models.RoomAvailability;
import com.example.hotel_bd.repository.ReservationRepository;
import com.example.hotel_bd.repository.RoomAvailabilityRepository;
import com.example.hotel_bd.repository.RoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * The RoomController is responsible for handling HTTP requests related to room management,
 * such as adding, removing, updating, and retrieving room information.
 *
 * It provides endpoints for the following operations:
 *
 * - Add a new room (accessible only to administrators)
 * - Remove a room by ID (accessible only to administrators)
 * - Update room details by ID (accessible only to administrators)
 * - Get a list of all rooms
 * - Get room details by ID
 */
@RestController
@RequestMapping("/api")
public class RoomController {
    @Autowired
    RoomRepository roomRepo;
    @Autowired
    RoomAvailabilityRepository availabilityRepo;
    @Autowired
    ReservationRepository reservationRepo;
    /**
     * Adds a new room to the repository. This method is accessible only to users with the ADMIN role.
     *
     * @param roomDTO the data transfer object containing the details of the room to be added, including capacity, amenities, type, and price per night
     * @return a ResponseEntity containing the room entity that was saved, wrapped in an HTTP response with a status of 200 OK
     */
    @PostMapping("/admin/room")
    public ResponseEntity<Room> addRoom(@RequestBody RoomDTO roomDTO) {
        Room room = new Room();
        room.setCapacity(roomDTO.getCapacity());
        room.setAmenities(roomDTO.getAmenities());
        room.setType(roomDTO.getType());
        room.setPricePerNight(roomDTO.getPricePerNight());

        roomRepo.save(room);
        return ResponseEntity.ok(room);
    }

    /**
     * Removes a room from the repository by its ID. This method is accessible only to users with the ADMIN role.
     *
     * @param id the ID of the room to be removed
     * @return a ResponseEntity containing a success message if the room was deleted,
     *         or a not found status if no room with the given ID exists
     */
    @DeleteMapping("/admin/room/{id}")
    @Transactional
    public ResponseEntity<String> removeRoom(@PathVariable Integer id) {
        Optional<Room> existingRoomOpt = roomRepo.findById(id);
        if (!existingRoomOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        List<RoomAvailability> roomAvailabilities = availabilityRepo.findByRoomId(id);
        if (!roomAvailabilities.isEmpty()) {
            for (RoomAvailability availability : roomAvailabilities) {
                availabilityRepo.delete(availability);
            }
        }
        List<Reservation> reservations = reservationRepo.findByRoomId(id);
        if(!reservations.isEmpty()) {
            for (Reservation reservation : reservations) {
                reservationRepo.delete(reservation);
            }
        }
        roomRepo.deleteById(id);
        return ResponseEntity.ok("Room deleted successfully.");
    }

    /**
     * Updates the details of an existing room based on the given room ID and the provided room data.
     * This method is accessible only to users with the ADMIN role.
     *
     * @param id the ID of the room to be updated
     * @param roomDTO a RoomDTO object containing the updated room information, including capacity, amenities, type, and price per night
     * @return a ResponseEntity containing the updated Room object if the update was successful,
     *         or a 404 Not Found status if no room with the given ID exists
     */
    @PutMapping("admin/room/{id}")
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

    /**
     * Retrieves a list of all rooms from the repository.
     *
     * @return a ResponseEntity containing a list of all Room objects, wrapped in
     *         an HTTP response with a status of 200 OK
     */
    @GetMapping("/user/room")
    public ResponseEntity<List<Room>> getAllRooms() {
        List<Room> rooms = roomRepo.findAll();
        return ResponseEntity.ok(rooms);
    }

    /**
     * Retrieves a room from the repository by its ID.
     *
     * @param id the ID of the room to be retrieved
     * @return a ResponseEntity containing the Room object if found, or a 404 Not Found status if no room with the given ID exists
     */
    @GetMapping("/user/room/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Integer id) {
        Optional<Room> existingRoomOpt = roomRepo.findById(id);
        if (existingRoomOpt.isPresent()) {
            Room existingRoom = existingRoomOpt.get();
            return ResponseEntity.ok(existingRoom);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/user/room/search")
    public ResponseEntity<List<Room>> searchRooms(
            @RequestParam(required = false) Integer roomType,
            @RequestParam(required = false) Integer capacity,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Date startDate,
            @RequestParam(required = false) Date endDate) {

        List<Room> availableRooms = roomRepo.findAvailableRoomsByFilters(roomType, capacity, maxPrice, startDate, endDate);
        return ResponseEntity.ok(availableRooms);
    }

}
