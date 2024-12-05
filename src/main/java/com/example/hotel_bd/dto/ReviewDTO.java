package com.example.hotel_bd.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class ReviewDTO {
    @NotNull
    @Size(min = 1, max = 255)
    private String content;
    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;
}
