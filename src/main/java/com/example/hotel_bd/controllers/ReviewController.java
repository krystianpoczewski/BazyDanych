package com.example.hotel_bd.controllers;

import com.example.hotel_bd.dto.ReviewDTO;
import com.example.hotel_bd.models.Review;
import com.example.hotel_bd.models.User;
import com.example.hotel_bd.repository.ReviewRepository;
import com.example.hotel_bd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;
    @PostMapping("/")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Review> addReview(@RequestBody ReviewDTO reviewDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<User> foundUser = userRepository.findByEmail(email);
        if (foundUser.isPresent()) {
            User user = foundUser.get();
            Review review = new Review();
            review.setUser(user);
            review.setDate(new Date());
            review.setRating(reviewDTO.getRating());
            review.setContent(reviewDTO.getContent());
            reviewRepository.save(review);
            return ResponseEntity.ok(review);
        }
        return ResponseEntity.notFound().build();
    }
    @PutMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateReview(@PathVariable Integer id, @RequestBody ReviewDTO reviewDTO) {
        Optional<Review> foundReview = reviewRepository.findById(id);
        if (foundReview.isPresent()) {
            Review review = foundReview.get();
            review.setRating(reviewDTO.getRating());
            review.setContent(reviewDTO.getContent());
            reviewRepository.save(review);
            return ResponseEntity.ok("Review updated");
        }
        return ResponseEntity.notFound().build();
    }
    @DeleteMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteReview(@PathVariable Integer id) {
        Optional<Review> foundReview = reviewRepository.findById(id);
        if (foundReview.isPresent()) {
            reviewRepository.delete(foundReview.get());
            return ResponseEntity.ok("Review deleted");
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/all")
    public ResponseEntity<List<Review>> getAllReviews() {
        return ResponseEntity.ok(reviewRepository.findAll());
    }
}
