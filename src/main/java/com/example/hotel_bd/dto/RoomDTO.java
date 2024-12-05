package com.example.hotel_bd.dto;

import com.example.hotel_bd.models.RoomAmenities;
import com.example.hotel_bd.models.RoomType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RoomDTO {
    @NotNull
    private RoomType type;
    @NotNull
    @Min(1)
    @Max(8)
    private Integer capacity;
    @NotNull
    private BigDecimal pricePerNight;
    @NotNull
    private List<RoomAmenities> amenities;
}
