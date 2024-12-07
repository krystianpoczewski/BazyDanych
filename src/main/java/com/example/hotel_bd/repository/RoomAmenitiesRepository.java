package com.example.hotel_bd.repository;

import com.example.hotel_bd.models.RoomAmenities;
import com.example.hotel_bd.models.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * RoomAmenitiesRepository is an interface for managing the persistence of RoomAmenities entities.
 * It extends JpaRepository to provide CRUD operations and additional methods for querying RoomAmenities data.
 *
 * The primary key for RoomAmenities is an Integer.
 *
 * Methods:
 * - findAll: Retrieves all RoomAmenities entities from the database.
 * - findById: Retrieves a RoomAmenities entity by its unique identifier.
 * - findByName: Retrieves a RoomAmenities entity by its unique name.
 */
public interface RoomAmenitiesRepository extends JpaRepository<RoomAmenities, Integer> {
    List<RoomAmenities> findAll();
    Optional<RoomAmenities> findById(int id);
    Optional<RoomAmenities> findByName(String name);
}
