package com.example.hotel.repository;

import com.example.hotel.model.Review;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer>{

    List<Review> findAll();
    @Query("SELECT r FROM Review r WHERE r.rating = :rating")
    List<Review> findByRating(@NotNull @Min(1) @Max(5) Integer rating);
    @Transactional
    @Query("SELECT r FROM Review r WHERE r.user = :userId")
    List<Review> findByUserId(Integer userId);

}