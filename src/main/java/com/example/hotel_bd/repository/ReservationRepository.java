package com.example.hotel_bd.repository;

import com.example.hotel_bd.models.Reservation;
import com.example.hotel_bd.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * ReservationRepository provides an interface for managing Reservation entities
 * in the persistence layer.
 *
 * It extends the JpaRepository interface to provide CRUD operations and
 * additional methods for retrieving reservations from the underlying data store.
 */
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findAll();
    @Query("SELECT rr FROM Reservation rr WHERE rr.user.email = :email")
    List<Reservation> findByUserEmail(String email);
    @Query("SELECT rr FROM Reservation rr JOIN rr.rooms r WHERE r.id = :roomId")
    List<Reservation> findByRoomId(Integer roomId);
    Optional<Reservation> findById(Integer id);
}
