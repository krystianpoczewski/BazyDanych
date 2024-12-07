package com.example.hotel_bd.dto;

import com.example.hotel_bd.models.ReservationAmenities;
import com.example.hotel_bd.models.Room;
import com.example.hotel_bd.models.User;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Data Transfer Object for handling reservation details.
 * This class is used to encapsulate the information related to a reservation, including
 * the user making the reservation, the dates of stay, and the rooms and amenities involved.
 *
 * Attributes:
 * - user: The user making the reservation, represented by a User object. This attribute is required.
 * - checkInDate: The date when the user is expected to check in. This attribute is required.
 * - checkOutDate: The date when the user is expected to check out. This attribute is required.
 * - rooms: A list of rooms included in the reservation. Each room is represented by a Room object.
 *   This attribute is required.
 * - amenities: An optional list of additional amenities associated with the reservation, represented
 *   by ReservationAmenities objects.
 *
 * The class uses Lombok's @Data annotation to automatically generate getters, setters, and other
 * utility methods.
 *
 * Note: Validation constraints are applied to ensure that all required fields are not null. This is
 * crucial for maintaining the integrity of the reservation data.
 */
@Data
public class ReservationDTO {
    @NotNull
    private User user;
    @NotNull
    private Date checkInDate;
    @NotNull
    private Date checkOutDate;
    @NotNull
    private List<Room> rooms;
    private List<ReservationAmenities> amenities;
}
