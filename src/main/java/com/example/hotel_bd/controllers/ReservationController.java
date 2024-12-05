package com.example.hotel_bd.controllers;

import com.example.hotel_bd.dto.ReservationDTO;
import com.example.hotel_bd.models.Reservation;
import com.example.hotel_bd.models.Room;
import com.example.hotel_bd.models.RoomAvailability;
import com.example.hotel_bd.models.User;
import com.example.hotel_bd.repository.ReservationRepository;
import com.example.hotel_bd.repository.RoomAvailabilityRepository;
import com.example.hotel_bd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoomAvailabilityRepository roomAvailabilityRepository;

    @PostMapping("/")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> createReservation(@RequestBody ReservationDTO reservationDTO) {
        if (isValidDate(reservationDTO))
            return ResponseEntity.badRequest().body("Reservation duration must be between 1 and 30 days.");

        List<Room> rooms = reservationDTO.getRooms();
        for (Room room : rooms) {
            List<RoomAvailability> overlapingReservations = roomAvailabilityRepository.findOverlappingReservations(room.getId(), reservationDTO.getCheckInDate(), reservationDTO.getCheckOutDate());
            if (!overlapingReservations.isEmpty()) {
                return ResponseEntity.badRequest().body("One or more rooms is not available in this date");
            }
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<User> foundUser = userRepository.findByEmail(email);
        if (foundUser.isPresent()) {
            User user = foundUser.get();

            Reservation reservation = new Reservation();
            reservation.setUser(user);
            reservation.setAmenities(reservationDTO.getAmenities());
            reservation.setCheckInDate(reservationDTO.getCheckInDate());
            reservation.setCheckOutDate(reservationDTO.getCheckOutDate());
            reservation.setRooms(reservationDTO.getRooms());

            reservationRepository.save(reservation);

            for(Room room : rooms) {
                RoomAvailability roomAvailability = new RoomAvailability();
                roomAvailability.setRoom(room);
                roomAvailability.setCheckInDate(reservationDTO.getCheckInDate());
                roomAvailability.setCheckOutDate(reservationDTO.getCheckOutDate());
                roomAvailabilityRepository.save(roomAvailability);
            }
            return ResponseEntity.ok("Reservation created successfully.");
        }
        else {
            return ResponseEntity.badRequest().body("User not found.");
        }
    }
    @GetMapping("/")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Reservation>> getAllUserReservations() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        if (userRepository.findByEmail(email).isPresent()) {
            List<Reservation> reservations = reservationRepository.findByUser(email);
            return ResponseEntity.ok(reservations);
        }
            return ResponseEntity.badRequest().body(null);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateReservation(@PathVariable Integer id, @RequestBody ReservationDTO reservationDTO) {
        if (isValidDate(reservationDTO))
            return ResponseEntity.badRequest().body("Reservation duration must be between 1 and 30 days.");
        List<Room> rooms = reservationDTO.getRooms();
        for (Room room : rooms) {
            List<RoomAvailability> overlapingReservations = roomAvailabilityRepository.findOverlappingReservations(room.getId(), reservationDTO.getCheckInDate(), reservationDTO.getCheckOutDate());
            if (!overlapingReservations.isEmpty()) {
                return ResponseEntity.badRequest().body("One or more rooms is not available in this date");
            }
        }
        Optional<Reservation> foundReservation = reservationRepository.findById(id);
        if (foundReservation.isPresent()) {
            for(Room room : rooms) {
                RoomAvailability roomAvailability = new RoomAvailability();
                roomAvailability.setRoom(room);
                roomAvailability.setCheckInDate(reservationDTO.getCheckInDate());
                roomAvailability.setCheckOutDate(reservationDTO.getCheckOutDate());

                Optional<RoomAvailability> roomAvailabilities = roomAvailabilityRepository.find(room.getId(), foundReservation.get().getCheckInDate(), foundReservation.get().getCheckOutDate());
                if(roomAvailabilities.isPresent()) {
                    roomAvailability.setId(roomAvailabilities.get().getId());
                }
                roomAvailabilityRepository.save(roomAvailability);
            }

            Reservation reservation = foundReservation.get();
            reservation.setAmenities(reservationDTO.getAmenities());
            reservation.setCheckInDate(reservationDTO.getCheckInDate());
            reservation.setCheckOutDate(reservationDTO.getCheckOutDate());
            reservation.setRooms(reservationDTO.getRooms());
            reservationRepository.save(reservation);
            return ResponseEntity.ok("Reservation updated successfully.");
        }
            return ResponseEntity.badRequest().body("Reservation not found.");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteReservation(@PathVariable Integer id) {
        Optional<Reservation> foundReservation = reservationRepository.findById(id);
        if (foundReservation.isPresent()) {
            for(Room room : foundReservation.get().getRooms()) {
                Optional<RoomAvailability> roomAvailabilities = roomAvailabilityRepository.find(room.getId(), foundReservation.get().getCheckInDate(), foundReservation.get().getCheckOutDate());
                if(roomAvailabilities.isPresent()) {
                    roomAvailabilityRepository.delete(roomAvailabilities.get());
                }
            }
            reservationRepository.delete(foundReservation.get());
            return ResponseEntity.ok("Reservation deleted successfully.");
        }
        return ResponseEntity.badRequest().body("Reservation not found.");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return ResponseEntity.ok(reservations);
    }

    private boolean isValidDate(@RequestBody ReservationDTO reservationDTO) {
        long durationInMillis = reservationDTO.getCheckOutDate().getTime() - reservationDTO.getCheckInDate().getTime();
        long durationInDays = durationInMillis / (1000 * 60 * 60 * 24);

        return durationInDays < 1 || durationInDays > 30;
    }
}
