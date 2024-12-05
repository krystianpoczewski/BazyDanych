package com.example.hotel_bd.controllers;

import com.example.hotel_bd.dto.UserDTO;
import com.example.hotel_bd.models.User;
import com.example.hotel_bd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class RegisterController {
    @Autowired
    UserRepository userRepository;
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO) {
        if(userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("email already in use");
        };
        User user = new User(userDTO.getFirstName(), userDTO.getLastName(),
                             userDTO.getPassword(), userDTO.getEmail());

        userRepository.save(user);
        return ResponseEntity.ok().body("successfully registered a user");
    }
}
