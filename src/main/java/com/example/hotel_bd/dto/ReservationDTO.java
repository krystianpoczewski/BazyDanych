package com.example.hotel_bd.dto;

import com.example.hotel_bd.models.ReservationAmenities;
import com.example.hotel_bd.models.Room;
import com.example.hotel_bd.models.User;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;

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
