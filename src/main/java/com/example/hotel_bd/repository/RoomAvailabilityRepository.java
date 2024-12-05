package com.example.hotel_bd.repository;

import com.example.hotel_bd.models.RoomAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface RoomAvailabilityRepository extends JpaRepository<RoomAvailability, Integer> {
    List<RoomAvailability> findAll();
    @Query("SELECT ra FROM RoomAvailability ra WHERE ra.room.id = :roomId " +
            "AND ((ra.checkInDate <= :endDate AND ra.checkOutDate >= :startDate))")
    List<RoomAvailability> findOverlappingReservations(@Param("roomId") Integer roomId,
                                                       @Param("startDate") Date startDate,
                                                       @Param("endDate") Date endDate);
    @Query("SELECT ra FROM RoomAvailability ra WHERE ra.room.id = :roomId " +
            "AND ((ra.checkInDate = :endDate AND ra.checkOutDate = :startDate))")
    Optional<RoomAvailability> find(@Param("roomId") Integer roomId,
                                           @Param("startDate") Date startDate,
                                           @Param("endDate") Date endDate);
}
