package com.example.hotel_bd.repository;

import com.example.hotel_bd.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findAll();
    Optional<Review> findById(int id);
    @Query("SELECT r FROM Review r WHERE r.user = :userId")
    List<Review> findByUserId(Integer userId);
}
