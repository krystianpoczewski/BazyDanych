package com.example.hotel_bd.repository;

import com.example.hotel_bd.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    List<Reservation> findByUser(String email);

}
