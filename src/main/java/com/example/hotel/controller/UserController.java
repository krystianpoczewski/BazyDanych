package com.example.hotel.controller;

import com.example.hotel.dto.UserDTO;
import com.example.hotel.model.User;
import com.example.hotel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /** Rejestracja użytkownika */
    @PostMapping("/register")
    public User registerUser(@RequestBody UserDTO userDTO) {
        return userService.registerUser(userDTO);
    }

    /** Zmiana hasła */
    @PutMapping("/change-password")
    public User changePassword(@RequestParam String email, @RequestParam String newPassword) {
        return userService.changePassword(email, newPassword);
    }

    /** Pobieranie danych użytkownika po ID */
    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Integer id) {
        return userService.findUserById(id);
    }

    /** Logowanie użytkownika (tylko wyszukiwanie na podstawie email) */
    @GetMapping("/login")
    public User login(@RequestParam String email) {
        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isPresent()) {
            return userOpt.get();
        }
        throw new RuntimeException("User not found");
    }
}

