package com.example.hotel.repository;

import com.example.hotel.model.Review;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer>{

    List<Review> findAll();
    @Query("SELECT r FROM Review r WHERE r.rating = :rating")
    List<Review> findByRating(@NotNull @Min(1) @Max(5) Integer rating);
    @Transactional
    @Query("SELECT r FROM Review r WHERE r.user = :userId")
    List<Review> findByUserId(Integer userId);
    /**dodanie opinii*/
    @Modifying
    @Transactional
    @Query("INSERT INTO Review (date, content, rating, user) VALUES (:date, :content, :rating, :user)")
    void saveReview(@NotNull java.util.Date date, @NotNull String content, @NotNull @Min(1) @Max(5) Integer rating, @NotNull com.example.hotel.model.User user);
    /** usunięcie opinii */
    @Modifying
    @Transactional
    @Query("DELETE FROM Review r WHERE r.id = :id")
    void deleteReviewById(Integer id);
}