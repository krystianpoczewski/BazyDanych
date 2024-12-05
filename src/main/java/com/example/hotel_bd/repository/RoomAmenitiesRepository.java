package com.example.hotel_bd.repository;

import com.example.hotel_bd.models.RoomAmenities;
import com.example.hotel_bd.models.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomAmenitiesRepository extends JpaRepository<RoomAmenities, Integer> {
    List<RoomAmenities> findAll();
    Optional<RoomAmenities> findById(int id);
    Optional<RoomAmenities> findByName(String name);
}
