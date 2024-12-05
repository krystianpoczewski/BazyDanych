package com.example.hotel_bd.repository;

import com.example.hotel_bd.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findAll();

    Optional<User> findByEmail(String email);


}
