package com.example.hotel_bd.dto;

/**
 * The AuthResponse class is a data transfer object that carries authentication response data,
 * particularly a JWT token generated upon successful authentication.
 *
 * This class provides methods to retrieve and optionally modify the token.
 */
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
