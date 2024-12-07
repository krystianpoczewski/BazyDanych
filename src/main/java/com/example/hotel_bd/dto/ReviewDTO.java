package com.example.hotel_bd.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

/**
 * ReviewDTO is a data transfer object that carries review information for a specific context,
 * such as a hotel or product review. It includes the textual content of the review and a
 * rating score.
 *
 * The content property is required and must have a length between 1 and 255 characters. This
 * ensures that the review has meaningful content without being excessively long.
 *
 * The rating property is mandatory and must be an integer between 1 and 5, inclusive. This represents
 * a typical rating scale where 1 indicates the lowest rating and 5 indicates the highest.
 *
 * This class uses validation annotations to enforce these constraints, ensuring that any
 * review data passed within the system maintains expected standards of quality and format.
 *
 * It uses Lombok's @Data annotation to automatically generate boilerplate code for getters,
 * setters, and other utility methods, facilitating easier handling of review data.
 */
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
