package com.example.hotel_bd.repository;

import com.example.hotel_bd.models.ReservationAmenities;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * ReservationAmenitiesRepository provides the mechanism for storage, retrieval,
 * update, and deletion of ReservationAmenities objects.
 * Extends JpaRepository to provide JPA specific methods.
 */
public interface ReservationAmenitiesRepository extends JpaRepository<ReservationAmenities, Integer> {
    List<ReservationAmenities> findAll();
    Optional<ReservationAmenities> findById(int id);
    Optional<ReservationAmenities> findByName(String name);
}
