package com.example.hotel_bd.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    private final String SECRET_KEY = "your-secret-key"; // Replace with a secure key

    /**
     * Extracts the username from a given JSON Web Token (JWT).
     * This method uses the token to retrieve the subject, which is typically the username.
     *
     * @param token the JWT from which the username will be extracted
     * @return the username contained in the token's subject claim
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from a JSON Web Token (JWT) using a claims resolver function.
     *
     * @param <T> the type of the claim to be extracted
     * @param token the JWT from which the claim will be extracted
     * @param claimsResolver a function that processes the extracted claims to retrieve the desired claim
     * @return the value of the specific claim extracted from the JWT
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from a JSON Web Token (JWT).
     *
     * @param token the JWT from which all claims will be extracted
     * @return the claims extracted from the JWT
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    /**
     * Generates a JSON Web Token (JWT) for a given user based on their details.
     * This method creates a token that includes claims about the user.
     *
     * @param userDetails the user details from which the token will be generated; typically includes the username and other user-specific properties
     * @return the generated JWT as a String
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Creates a JSON Web Token (JWT) using the specified claims and subject.
     * The token includes custom claims provided in the parameter, sets the subject,
     * and sets the issue and expiration dates.
     *
     * @param claims a map of claims to be included in the token payload
     * @param subject the subject for which the token is being created; typically a username
     * @return a signed JWT string containing the specified claims and subject
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * Validates a JSON Web Token (JWT) against the provided user details.
     * It checks if the token's username matches the given user details'
     * username and ensures that the token is not expired.
     *
     * @param token the JWT to be validated
     * @param userDetails the user details to compare with the token's username
     * @return true if the token is valid (i.e., the username matches and the token is not expired), false otherwise
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Checks if the JSON Web Token (JWT) provided is expired.
     *
     * This method extracts the expiration date from the token and compares it with the current date to determine if the token is expired.
     *
     * @param token the JWT to be checked for expiration
     * @return true if the token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from a given JSON Web Token (JWT).
     *
     * @param token the JWT from which the expiration date will be extracted
     * @return the expiration date of the token
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
