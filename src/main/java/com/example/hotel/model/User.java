package com.example.hotel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @NotNull
    @Size(min = 2, max = 30)
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Size(min = 2, max = 50)
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Column(name = "hashed_password")
    private String hashedPassword;

    @NotNull
    @Email
    @Column(name = "email")
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    private String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    public User() {}

    public User(Role role, String email, String password, String lastName, String firstName) {
        this.role = role;
        this.email = email;
        setPassword(password);
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setPassword(String plainPassword) {
        this.hashedPassword = hashPassword(plainPassword);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}

enum Role{
    USER,
    ADMIN
}