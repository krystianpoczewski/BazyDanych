package com.example.hotel_bd.repository;

import com.example.hotel_bd.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findAll();
    @Query("SELECT rr FROM Reservation rr WHERE rr.user.email = :email")
    List<Reservation> findByUser(String email);

}
