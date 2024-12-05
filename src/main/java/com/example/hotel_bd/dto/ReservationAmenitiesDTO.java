package com.example.hotel_bd.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReservationAmenitiesDTO {
    @NotNull
    @Size(min = 1, max = 50)
    private String name;
    @NotNull
    @Min(0)
    private BigDecimal pricePerNight;
}
