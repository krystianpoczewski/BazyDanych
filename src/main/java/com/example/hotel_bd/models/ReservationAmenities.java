package com.example.hotel_bd.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * The ReservationAmenities class represents the amenities associated with a reservation.
 * Each amenity has a unique identifier, a name, and a price per night.
 *
 * This class is annotated with JPA annotations to map it to a database entity.
 *
 * Attributes include:
 * - id: A unique identifier for the amenity.
 * - name: The name of the amenity, which must be unique and non-null.
 * - pricePerNight: The price per night for the amenity, which must be non-null and non-negative.
 */
@Entity
public class ReservationAmenities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_amenities_id")
    private Integer id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(unique = true, name = "name")
    private String name;

    @NotNull
    @Min(0)
    @Column(name = "price_per_night")
    private BigDecimal pricePerNight;

    public ReservationAmenities() {}

    public ReservationAmenities(String name, BigDecimal pricePerNight) {
        this.name = name;
        this.pricePerNight = pricePerNight;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ReservationAmenities{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pricePerNight=" + pricePerNight +
                '}';
    }
}
