package com.example.hotel_bd.repository;

import com.example.hotel_bd.models.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for handling CRUD operations and custom queries related to the Review entity.
 * Extends the JpaRepository to provide JPA functionality.
 */
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findAll();
    Page<Review> findAll(Pageable pageable);
    Optional<Review> findById(int id);
    @Query("SELECT r FROM Review r WHERE r.user.id = :userId")
    List<Review> findByUserId(Integer userId);
}
