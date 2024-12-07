package com.example.hotel_bd.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * SecurityConfiguration is a configuration class for setting up web security using Spring Security.
 * It configures CORS settings, CSRF protection, request authorization rules, and an authentication manager.
 * It also integrates JWT-based authentication by incorporating a custom JwtAuthenticationFilter.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(
            AuthenticationProvider authenticationProvider,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Configures a security filter chain using Spring Security to manage CORS settings, disable CSRF protection,
     * authorize HTTP requests, and integrate authentication mechanisms.
     *
     * This method sets up the necessary filters and providers to handle authentication and authorization for the application.
     * It allows public access to login and registration endpoints while securing other resources by requiring authentication.
     * Additionally, it configures the custom JWT authentication filter before the UsernamePasswordAuthenticationFilter in the filter chain.
     *
     * @param http the HttpSecurity instance used to configure the security settings of the application
     * @return a configured instance of SecurityFilterChain to be used by the Spring Security framework
     * @throws Exception if an error occurs while setting up the security configurations
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login/**", "/register/**", "/**").permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(this.authenticationProvider)
                .addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }

    /**
     * Configures the Cross-Origin Resource Sharing (CORS) settings for the application.
     *
     * This method sets up a CORS configuration that allows requests from specific origins
     * and limits the HTTP methods and headers that can be used in CORS requests. It registers
     * this configuration to apply to all endpoint paths.
     *
     * The specific configurations include:
     * - Allowed Origins: http://localhost:8080, http://localhost
     * - Allowed Methods: GET, POST, OPTIONS
     * - Allowed Headers: Authorization, Content-Type
     *
     * @return a configured instance of CorsConfigurationSource that handles CORS requests for the application
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:8080", "http://localhost"));
        configuration.setAllowedMethods(List.of("GET", "POST", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization","Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    /**
     * Configures and provides an instance of AuthenticationManager.
     *
     * This method sets up the AuthenticationManager by retrieving the shared
     * AuthenticationManagerBuilder from the HttpSecurity context. It further
     * configures this builder with a custom AuthenticationProvider and constructs
     * the AuthenticationManager instance to be used in the application for managing
     * authentication processes.
     *
     * @param http the HttpSecurity instance that provides access to shared objects,
     *             including the AuthenticationManagerBuilder, allowing configuration of
     *             the authentication management.
     * @return the fully configured AuthenticationManager instance.
     * @throws Exception if an error occurs while building the AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(this.authenticationProvider)
                .build();
    }
}
