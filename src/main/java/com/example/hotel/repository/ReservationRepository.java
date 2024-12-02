package com.example.hotel.repository;

import com.example.hotel.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer>{

    List<Reservation> findAll();
    @Transactional
    @Query("SELECT rr FROM Reservation rr WHERE rr.user = :userId")
    List<Reservation> findByUser(Integer userId);
    @Transactional
    @Query("SELECT rr FROM Reservation rr WHERE rr.rooms = :roomId")
    List<Reservation> findByRoom(Integer roomId);
    @Transactional
    @Query("SELECT COUNT(DISTINCT rr.user), COUNT(rr.rooms), COUNT(rr.amenities) FROM Reservation rr")
    List<Object[]> findReservationDetails();


}
