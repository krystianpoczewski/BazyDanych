package com.example.hotel_bd.repository;

import com.example.hotel_bd.models.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing RoomType entities.
 * This interface extends JpaRepository to provide basic CRUD operations
 * and includes custom query methods to find RoomTypes by their unique properties.
 *
 * @param <RoomType> the entity type managed by the repository
 * @param <Integer> the primary key type for the RoomType entity
 */
public interface RoomTypeRepository extends JpaRepository<RoomType, Integer> {
    List<RoomType> findAll();
    Optional<RoomType> findById(int id);
    Optional<RoomType> findByName(String name);
}
