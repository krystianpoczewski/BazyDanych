package com.example.hotel.repository;

import com.example.hotel.model.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findAll();
    Optional<User> findById(Integer id);
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.firstName = :firstName")
    Optional<User> findByFirstName(@NotNull @Size(min = 2, max = 30) String firstName);
    @Query("SELECT u FROM User u WHERE u.lastName = :lastName")
    Optional<User> findByLastName(@NotNull @Size(min = 2, max = 30) String lastName);

}
