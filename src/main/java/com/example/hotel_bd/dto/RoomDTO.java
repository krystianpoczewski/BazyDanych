package com.example.hotel_bd.dto;

import com.example.hotel_bd.models.RoomAmenities;
import com.example.hotel_bd.models.RoomType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * RoomDTO is a data transfer object representing details of a room in a hotel management system.
 * It contains information about the room type, capacity, pricing, and available amenities.
 *
 * Attributes:
 * - type: Denotes the type of the room, which must be specified. This is a required field.
 * - capacity: Specifies the maximum number of occupants the room can accommodate. It is required
 *   and must be between 1 and 8, inclusive.
 * - pricePerNight: Indicates the cost of staying in the room for one night. This is a required field.
 * - amenities: A list of RoomAmenities that are available with the room. This is a required field.
 *
 * This class makes use of validation annotations to enforce that the type, capacity, pricePerNight,
 * and amenities fields are populated with non-null values, and that the capacity is within the specified
 * range, ensuring data integrity and consistency.
 *
 * The class leverages Lombok's @Data annotation to automatically generate boilerplate code
 * like getters, setters, and other utility methods for ease of use and maintenance.
 */
@Data
public class RoomDTO {
    @NotNull
    private RoomType type;
    @NotNull
    @Min(1)
    @Max(8)
    private Integer capacity;
    @NotNull
    @Min(0)
    private BigDecimal pricePerNight;
    @NotNull
    private List<RoomAmenities> amenities;

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public List<RoomAmenities> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<RoomAmenities> amenities) {
        this.amenities = amenities;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
