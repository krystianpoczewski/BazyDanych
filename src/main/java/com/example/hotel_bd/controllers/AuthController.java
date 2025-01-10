package com.example.hotel_bd.controllers;

import com.example.hotel_bd.dto.AuthRequest;
import com.example.hotel_bd.dto.AuthResponse;
import com.example.hotel_bd.dto.ChangePasswordRequest;
import com.example.hotel_bd.dto.UserDTO;
import com.example.hotel_bd.models.User;
import com.example.hotel_bd.models.UserRole;
import com.example.hotel_bd.repository.UserRepository;
import com.example.hotel_bd.utils.JwtUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController is a REST controller that manages authentication processes.
 * It handles user login requests and generates JWT tokens for authenticated users.
 */
@RestController
@RequestMapping
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            String token = jwtUtils.generateToken(request.getEmail(), authentication.getAuthorities().toString());

            ResponseCookie cookie = ResponseCookie.from("jwt", token)
                    .httpOnly(false)
                    .secure(true)
                    .path("/")
                    .maxAge(24 * 60 * 60)
                    .sameSite("Strict")
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(new AuthResponse(token));
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid credentials");
        }
    }


    @PostMapping("/auth/register")
    public ResponseEntity<String> register(@RequestBody UserDTO request) {
        var existing = userRepository.findByEmail(request.getEmail());
        if(existing.isPresent()){
            throw new IllegalArgumentException("Email is already in use");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setHashedPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(request.getRole() != null ? request.getRole() : UserRole.USER);
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/user/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getHashedPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        user.setHashedPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Password updated successfully");
    }
}
