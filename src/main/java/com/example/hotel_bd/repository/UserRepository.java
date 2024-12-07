package com.example.hotel_bd.repository;

import com.example.hotel_bd.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * UserRepository is an interface for managing the persistence and retrieval
 * of User entities. It extends JpaRepository to provide standard CRUD operations
 * along with custom query methods specific to User entities.
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findAll();
    Optional<User> findByEmail(String email);


}
