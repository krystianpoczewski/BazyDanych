package com.example.hotel_bd.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

/**
 * Represents a review entity with attributes for a user, date, content, and rating.
 * The review is associated with a specific user and includes textual content
 * and a rating score.
 *
 * This entity is annotated to be used with JPA for ORM (Object-Relational Mapping)
 * and includes constraints for ensuring data validity:
 * - The user associated with the review cannot be null.
 * - The content of the review should be between 1 and 255 characters.
 * - The rating must be an integer between 1 and 5.
 *
 * The Review class provides getter and setter methods to access and modify
 * its attributes, and a custom toString implementation for easy representation.
 */
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(name = "review_date")
    private Date date;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "content")
    private String content;

    @NotNull
    @Min(1)
    @Max(5)
    @Column(name = "rating")
    private Integer rating;

    public Review() {}

    public Review(User user, Date date, String content, Integer rating) {
        this.user = user;
        this.date = date;
        this.content = content;
        this.rating = rating;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", user=" + user +
                ", date=" + date +
                ", content='" + content + '\'' +
                ", rating=" + rating +
                '}';
    }
}
