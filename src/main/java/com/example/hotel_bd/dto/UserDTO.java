package com.example.hotel_bd.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * UserDTO is a data transfer object that represents user details within the system.
 * It includes commonly required user information such as first name, last name, email, and password.
 *
 * Attributes:
 * - firstName: The given name of the user. This is a required field with a length constraint of
 *   between 2 and 30 characters.
 * - lastName: The family name of the user. This is a required field with a length constraint of
 *   between 2 and 50 characters.
 * - email: The user's email address. This is a required field and must be formatted as a valid
 *   email address.
 * - password: The user's password. This is a required field.
 *
 * The class uses validation annotations to enforce the presence and correctness of each field,
 * ensuring that user data remains consistent and reliable.
 *
 * Lombok's @Data annotation is utilized to automatically generate necessary methods such as
 * getters, setters, equals, hashCode, and toString, which facilitates seamless integration
 * and manipulation of user data.
 */
@Data
public class UserDTO {
    @NotNull
    @Size(min = 2, max = 30)
    private String firstName;
    @NotNull
    @Size(min = 2, max = 50)
    private String lastName;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String password;
}
