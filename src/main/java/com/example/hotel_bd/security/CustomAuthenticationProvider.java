package com.example.hotel_bd.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * CustomAuthenticationProvider is a custom implementation of the AuthenticationProvider interface.
 * It is responsible for authenticating users based on their username and password.
 *
 * This class retrieves user details using the provided UserDetailsService and verifies the password
 * using the given PasswordEncoder. If authentication is successful, it returns an authenticated
 * UsernamePasswordAuthenticationToken.
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a CustomAuthenticationProvider with the specified UserDetailsService and PasswordEncoder.
     *
     * @param userDetailsService the UserDetailsService used to retrieve user details
     * @param passwordEncoder the PasswordEncoder used to verify user passwords
     */
    public CustomAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Authenticates a user based on the provided Authentication object.
     * This method retrieves user details using the UserDetailsService and checks
     * if the provided password matches the stored password using the PasswordEncoder.
     *
     * @param authentication the Authentication object containing the user's authentication request
     *                       information, including username and password.
     * @return an authenticated UsernamePasswordAuthenticationToken containing the user's details
     *         and granted authorities if authentication is successful.
     * @throws AuthenticationException if the authentication fails due to invalid credentials.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            return new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
        } else {
            throw new AuthenticationException("Invalid username or password") {};
        }
    }

    /**
     * Determines if the specified authentication type is supported by this provider.
     *
     * @param authentication the class of the authentication object to check
     * @return true if the specified authentication class is compatible with
     *         UsernamePasswordAuthenticationToken, false otherwise
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
