package com.example.hotel_bd.dto;

public class AuthResponse {
    private String token;

    public AuthResponse(String token) {
        this.token = token;
    }

    // Getter
    public String getToken() {
        return token;
    }

    // Setter (optional)
    public void setToken(String token) {
        this.token = token;
    }
}