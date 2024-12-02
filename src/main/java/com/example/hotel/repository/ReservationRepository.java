package com.example.hotel.repository;

import com.example.hotel.model.Reservation;
import com.example.hotel.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer>{

    List<Reservation> findAll();
    @Transactional
    @Query("SELECT rr FROM Reservation rr WHERE rr.user = :userId")
    List<Reservation> findByUser(Integer userId);
    @Transactional
    @Query("SELECT rr FROM Reservation rr WHERE rr.rooms = :roomId")
    List<Reservation> findByRoom(Integer roomId);

}
