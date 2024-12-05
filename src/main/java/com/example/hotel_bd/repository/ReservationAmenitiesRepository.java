package com.example.hotel_bd.repository;

import com.example.hotel_bd.models.ReservationAmenities;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationAmenitiesRepository extends JpaRepository<ReservationAmenities, Integer> {
    List<ReservationAmenities> findAll();
    Optional<ReservationAmenities> findById(int id);
    Optional<ReservationAmenities> findByName(String name);
}
