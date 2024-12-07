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

    /**
     * Creates a new reservation for a user based on the provided reservation details.
     * This method checks if the reservation dates are valid and if the selected rooms are available.
     * If the reservation is successfully created, it will store the reservation and update room availability.
     *
     * @param reservationDTO the data transfer object containing reservation details including amenities, check-in
     *                       and check-out dates, and rooms to be reserved
     * @return a ResponseEntity containing a confirmation message if the reservation is successfully created, or
     *         an error message if the reservation dates are invalid, if rooms are not available, or if the user
     *         is not found
     */
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
    /**
     * Retrieves all reservations associated with the authenticated user.
     *
     * @return ResponseEntity containing a list of reservations if the user is found,
     *         otherwise a ResponseEntity with a bad request status.
     */
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
    /**
     * Updates an existing reservation in the system. This method validates the reservation details,
     * ensures that the rooms in the reservation are available for the specified dates, and updates the reservation
     * in the database if the validation passes. The user must have 'ADMIN' role to perform this operation.
     *
     * @param id the ID of the reservation to be updated
     * @param reservationDTO a DTO containing the updated details of the reservation including check-in and check-out dates, rooms, and amenities
     * @return a ResponseEntity containing a success message if the reservation is updated, or an error message if the update fails
     */
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

    /**
     * Deletes a reservation with the specified ID along with associated room availabilities.
     *
     * @param id the unique identifier of the reservation to be deleted
     * @return a ResponseEntity containing a success message if the reservation was successfully
     *         deleted, or an error message if the reservation was not found
     */
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

    /**
     * Retrieves all reservations from the database.
     * This method is accessible only to users with the 'ADMIN' role.
     *
     * @return a ResponseEntity containing a list of all reservations
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return ResponseEntity.ok(reservations);
    }

    /**
     * Checks if the reservation dates are within a valid range.
     *
     * The method calculates the duration, in days, between the check-in and check-out dates
     * specified in the ReservationDTO. It validates whether this duration is less than 1 day or greater than 30 days.
     *
     * @param reservationDTO the data transfer object containing the check-in and check-out dates
     * @return true if the duration is less than 1 day or greater than 30 days, false otherwise
     */
    private boolean isValidDate(@RequestBody ReservationDTO reservationDTO) {
        long durationInMillis = reservationDTO.getCheckOutDate().getTime() - reservationDTO.getCheckInDate().getTime();
        long durationInDays = durationInMillis / (1000 * 60 * 60 * 24);

        return durationInDays < 1 || durationInDays > 30;
    }
}
