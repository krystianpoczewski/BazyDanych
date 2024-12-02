package com.example.hotel.repository;

import com.example.hotel.model.RoomAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RoomAvailabilityRepository extends JpaRepository<RoomAvailability, Integer> {
    @Query ("SELECT ra FROM RoomAvailability ra WHERE ra.room.id = :roomId " +
            "AND ((ra.checkInDate <= :endDate AND ra.checkOutDate >= :startDate))")
    List<RoomAvailability> findOverlappingReservations(@Param("roomId") Integer roomId,
                                                       @Param("startDate") Date startDate,
                                                       @Param("endDate") Date endDate);
}
