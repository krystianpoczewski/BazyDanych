package com.example.hotel_bd.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

/**
 * The ReservationAmenitiesDTO class is a data transfer object used for transferring
 * reservation amenity data. It primarily includes the name of the amenity and the
 * price per night associated with it.
 *
 * The name property is validated to ensure it is not null and has a length between 1 and 50 characters.
 * The pricePerNight property is validated to ensure it is not null and is a non-negative value.
 */
@Data
public class ReservationAmenitiesDTO {
    @NotNull
    @Size(min = 1, max = 50)
    private String name;
    @NotNull
    @Min(0)
    private BigDecimal pricePerNight;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }
}
