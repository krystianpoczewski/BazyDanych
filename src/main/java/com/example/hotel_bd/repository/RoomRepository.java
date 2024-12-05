package com.example.hotel_bd.repository;

import com.example.hotel_bd.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    List<Room> findAll();
    Optional<Room> findById(int id);
    @Query("SELECT r FROM Room r WHERE " +
            "(:roomType IS NULL OR r.type.id = :roomType) AND " +
            "(:capacity IS NULL OR r.capacity >= :capacity) AND " +
            "(:maxPrice IS NULL OR r.pricePerNight <= :maxPrice) AND " +
            "NOT EXISTS (" +
            "   SELECT ra FROM RoomAvailability ra " +
            "   WHERE ra.room.id = r.id AND " +
            "   ((ra.checkInDate <= :endDate AND ra.checkOutDate >= :startDate))" +
            ")")
    List<Room> findAvailableRoomsByFilters(@Param("roomType") Integer roomType,
                                           @Param("capacity") Integer capacity,
                                           @Param("maxPrice") BigDecimal maxPrice,
                                           @Param("startDate") Date startDate,
                                           @Param("endDate") Date endDate);
}
