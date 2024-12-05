package com.example.hotel_bd.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

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
