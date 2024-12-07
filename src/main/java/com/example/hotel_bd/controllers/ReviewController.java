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

/**
 * ReviewController manages CRUD operations for reviews.
 * It handles requests related to adding, updating, deleting,
 * and retrieving reviews from the repository.
 *
 * Methods:
 *
 * - addReview: Allows a user with the 'USER' role to add a new review.
 * - updateReview: Allows an administrator to update an existing review.
 * - deleteReview: Allows an administrator to delete an existing review.
 * - getAllReviews: Retrieves a list of all reviews.
 *
 * Annotations:
 *
 * - @RestController: Indicates that this class is a REST controller.
 * - @RequestMapping: Maps HTTP requests to handler methods of MVC and REST controllers.
 * - @Autowired: Injects the required repository instances into the controller.
 * - @PreAuthorize: Specifies security roles required to access a method.
 */
@RestController
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;
    /**
     * Adds a new review to the system. This method allows a user with the 'USER' role
     * to submit a review. It retrieves the currently authenticated user's email,
     * finds the corresponding user in the database, and associates the review with this user.
     * The review's date is set to the current date.
     *
     * @param reviewDTO the data transfer object containing the review's content and rating
     * @return a ResponseEntity containing the saved review if the user is found,
     *         otherwise a ResponseEntity with a 'not found' status
     */
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
    /**
     * Updates an existing review in the system. This method is only accessible to users with the 'ADMIN' role.
     * It updates the rating and content of an existing review identified by its ID.
     *
     * @param id the unique identifier of the review to be updated
     * @param reviewDTO the data transfer object containing updated review details such as its content and rating
     * @return a ResponseEntity with a message "Review updated" if the update is successful,
     *         otherwise a ResponseEntity with a 'not found' status if the review does not exist
     */
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
    /**
     * Deletes an existing review from the system. This method is accessible only to users with the 'ADMIN' role.
     * It removes a review identified by its unique ID from the repository.
     *
     * @param id the unique identifier of the review to be deleted
     * @return a ResponseEntity with a message "Review deleted" if the deletion is successful,
     *         otherwise a ResponseEntity with a 'not found' status if the review does not exist
     */
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
    /**
     * Retrieves all reviews from the repository.
     *
     * @return a ResponseEntity containing a list of all Review objects
     */
    @GetMapping("/all")
    public ResponseEntity<List<Review>> getAllReviews() {
        return ResponseEntity.ok(reviewRepository.findAll());
    }
}
