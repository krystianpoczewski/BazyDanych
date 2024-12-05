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

@RestController
@RequestMapping("/reservation/amenities")
public class ReservationAmenitiesController {
    @Autowired
    ReservationAmenitiesRepository reservationAmenitiesRepository;

    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
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
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
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
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteReservationAmenities(@PathVariable Integer id) {
        Optional<ReservationAmenities> existingReservationAmenity = reservationAmenitiesRepository.findById(id);
        if(existingReservationAmenity.isEmpty()) {
            return ResponseEntity.badRequest().body("Reservation amenity not found");
        }
        reservationAmenitiesRepository.delete(existingReservationAmenity.get());
        return ResponseEntity.ok("Reservation amenity deleted");
    }
    @GetMapping("/")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ReservationAmenities>> getAllReservationAmenities() {
        List<ReservationAmenities> allReservationAmenities = reservationAmenitiesRepository.findAll();
        return ResponseEntity.ok(allReservationAmenities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationAmenities> getReservationAmenity(@PathVariable Integer id){
        Optional<ReservationAmenities> existingReservationAmenity = reservationAmenitiesRepository.findById(id);
        if(existingReservationAmenity.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(existingReservationAmenity.get());
    }
}

