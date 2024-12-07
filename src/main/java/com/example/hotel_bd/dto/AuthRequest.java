package com.example.hotel_bd.dto;

/**
 * The AuthRequest class represents a request for authentication, typically containing
 * a user's email and password. It is used to transfer authentication data to the server
 * during login or authentication processes.
 *
 * This class provides getter and setter methods for accessing and modifying the email and
 * password fields.
 */
public class AuthRequest {
    private String email;
    private String password;

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
