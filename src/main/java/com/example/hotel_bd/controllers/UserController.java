package com.example.hotel_bd.controllers;

import com.example.hotel_bd.models.User;
import com.example.hotel_bd.repository.ReviewRepository;
import com.example.hotel_bd.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * UserController is a REST controller that manages user resources.
 * It provides endpoints for performing CRUD operations on User entities.
 * The controller is accessible only to users with the ADMIN role.
 *
 * All requests are prefixed with "/api/users".
 */
@RestController
@RequestMapping("/api/admin/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Retrieves a list of all users from the database.
     *
     * @return a ResponseEntity containing a list of User objects,
     *         wrapped with an HTTP status 200 (OK) response.
     */
    @GetMapping()
    public ResponseEntity<List<User>> findAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address of the user to be retrieved
     * @return a ResponseEntity containing the found user or a not found response if the user does not exist
     */
    @GetMapping("/{email}")
    public ResponseEntity<User> findByEmail(@PathVariable String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<User> findById(@PathVariable Integer id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    /**
     * Updates the details of an existing user.
     *
     * @param id the unique identifier of the user to be updated
     * @param updatedUser the user object containing updated user details
     * @return a ResponseEntity containing the updated user object if the user is found, else a ResponseEntity with a not found status
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User updatedUser) {
        Optional<User> existingUserOpt = userRepository.findById(id);
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setRole(updatedUser.getRole());
            existingUser.setHashedPassword(passwordEncoder.encode(updatedUser.getHashedPassword()));
            User savedUser = userRepository.save(existingUser);
            return ResponseEntity.ok(savedUser);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Deletes a user from the system based on the provided user ID.
     * This action can only be performed by users with the ADMIN role.
     *
     * @param id the ID of the user to be deleted
     * @return a ResponseEntity containing a success message if the user is deleted;
     *         or a ResponseEntity with a not found status if the user does not exist
     */
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            reviewRepository.findByUserId(id).forEach(review -> reviewRepository.delete(review));
            return ResponseEntity.ok("User deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }
}
