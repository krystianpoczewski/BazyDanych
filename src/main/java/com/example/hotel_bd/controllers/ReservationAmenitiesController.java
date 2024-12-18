package com.example.hotel_bd.controllers;

import com.example.hotel_bd.dto.ReservationAmenitiesDTO;
import com.example.hotel_bd.models.ReservationAmenities;
import com.example.hotel_bd.repository.ReservationAmenitiesRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for managing reservation amenities.
 * Provides endpoints for creating, updating, deleting, and retrieving reservation amenities.
 */
@RestController
@RequestMapping("/api")
public class ReservationAmenitiesController {
    @Autowired
    ReservationAmenitiesRepository reservationAmenitiesRepository;

    /**
     * Adds a new reservation amenity to the system.
     * If an amenity with the same name already exists, it returns a bad request response.
     *
     * @param reservationAmenitiesDTO Data Transfer Object containing information about the reservation amenity to be added.
     * @return ResponseEntity containing the created ReservationAmenities object if successful,
     *         or a bad request response with the existing reservation amenity if a duplicate is found.
     */
    @PostMapping("/admin/reservation-amenities")
    public ResponseEntity<ReservationAmenities> addReservationAmenities(@RequestBody ReservationAmenitiesDTO reservationAmenitiesDTO) {
        Optional<ReservationAmenities> existingReservationAmenity = reservationAmenitiesRepository.findByName(reservationAmenitiesDTO.getName());
        if(existingReservationAmenity.isPresent()) {
            return ResponseEntity.badRequest().body(existingReservationAmenity.get());
        }
        ReservationAmenities reservationAmenities = new ReservationAmenities();
        reservationAmenities.setName(reservationAmenitiesDTO.getName());
        reservationAmenities.setPricePerNight(reservationAmenitiesDTO.getPricePerNight());
        reservationAmenitiesRepository.save(reservationAmenities);
        return ResponseEntity.ok(reservationAmenities);
    }
    /**
     * Updates an existing reservation amenity identified by the given ID with the details provided in the ReservationAmenitiesDTO.
     * The ID is used to find the existing reservation amenity. If found, the amenity's details are updated; otherwise,
     * a bad request response is returned.
     *
     * @param id The unique identifier of the reservation amenity to be updated.
     * @param reservationAmenitiesDTO Data Transfer Object containing the new details for the reservation amenity.
     * @return ResponseEntity containing the updated ReservationAmenities object if successful,
     *         or a bad request response if the reservation amenity is not found.
     */
    @PutMapping("/admin/reservation-amenities/{id}")
    public ResponseEntity<ReservationAmenities> updateReservationAmenities(@PathVariable Integer id, @RequestBody ReservationAmenitiesDTO reservationAmenitiesDTO) {
        Optional<ReservationAmenities> existingReservationAmenity = reservationAmenitiesRepository.findById(id);
        if(existingReservationAmenity.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        ReservationAmenities reservationAmenities = existingReservationAmenity.get();
        reservationAmenities.setId(id);
        reservationAmenities.setName(reservationAmenitiesDTO.getName());
        reservationAmenities.setPricePerNight(reservationAmenitiesDTO.getPricePerNight());
        reservationAmenitiesRepository.save(reservationAmenities);
        return ResponseEntity.ok(reservationAmenities);
    }
    /**
     * Deletes a reservation amenity identified by its unique ID.
     * Only users with the 'ADMIN' role are authorized to perform this operation.
     *
     * @param id the unique identifier of the reservation amenity to be deleted
     * @return a ResponseEntity with a message indicating the result of the deletion request;
     *         returns a bad request response if the reservation amenity is not found
     */
    @DeleteMapping("/admin/reservation-amenities/{id}")
    public ResponseEntity<String> deleteReservationAmenities(@PathVariable Integer id) {
        Optional<ReservationAmenities> existingReservationAmenity = reservationAmenitiesRepository.findById(id);
        if(existingReservationAmenity.isEmpty()) {
            return ResponseEntity.badRequest().body("Reservation amenity not found");
        }
        reservationAmenitiesRepository.delete(existingReservationAmenity.get());
        return ResponseEntity.ok("Reservation amenity deleted");
    }
    /**
     * Retrieves a list of all reservation amenities.
     *
     * @return a ResponseEntity containing a list of all ReservationAmenities objects
     */
    @GetMapping("/user/reservation-amenities")
    public ResponseEntity<List<ReservationAmenities>> getAllReservationAmenities() {
        List<ReservationAmenities> allReservationAmenities = reservationAmenitiesRepository.findAll();
        return ResponseEntity.ok(allReservationAmenities);
    }

    /**
     * Retrieves the reservation amenity associated with the specified ID.
     *
     * @param id the ID of the reservation amenity to retrieve
     * @return a ResponseEntity containing the ReservationAmenities object if found;
     *         otherwise, a ResponseEntity with a bad request status
     */
    @GetMapping("/user/reservation-amenities/{id}")
    public ResponseEntity<ReservationAmenities> getReservationAmenity(@PathVariable Integer id){
        Optional<ReservationAmenities> existingReservationAmenity = reservationAmenitiesRepository.findById(id);
        if(existingReservationAmenity.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(existingReservationAmenity.get());
    }
}

