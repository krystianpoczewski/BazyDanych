package com.example.hotel.repository;

import com.example.hotel.model.ReservationAmenities;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationAmenitiesRepository extends JpaRepository <ReservationAmenities, Integer> {
    Optional<ReservationAmenities> findByReservationId(Integer reservationId);
    Optional<ReservationAmenities> findByAmenitiesId(Integer amenitiesId);
}
