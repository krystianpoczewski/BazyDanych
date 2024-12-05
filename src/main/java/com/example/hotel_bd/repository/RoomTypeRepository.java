package com.example.hotel_bd.repository;

import com.example.hotel_bd.models.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomTypeRepository extends JpaRepository<RoomType, Integer> {
    List<RoomType> findAll();
    Optional<RoomType> findById(int id);
    Optional<RoomType> findByName(String name);
}
