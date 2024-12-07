package com.example.hotel_bd.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

/**
 * Represents the availability of a room within a certain date range.
 * This entity is used to track room bookings by associating a specific room
 * with a check-in and check-out date.
 *
 * The RoomAvailability entity includes information about the room,
 * check-in date, and check-out date. Functionalities include setting and getting
 * these values, as well as generating a string representation of the object.
 *
 * Each RoomAvailability instance corresponds to a single availability period
 * for a specific room.
 */
@Entity
public class RoomAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_availability_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @NotNull
    @Column(name = "check_in_date")
    private Date checkInDate;

    @NotNull
    @Column(name = "check_out_date")
    private Date checkOutDate;

    public RoomAvailability() {}

    public RoomAvailability(Date checkInDate, Date checkOutDate, Room room) {
        if (checkInDate == null || checkOutDate == null || checkInDate.after(checkOutDate)) {
            throw new IllegalArgumentException("Check-in date must be before check-out date.");
        }
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.room = room;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "RoomAvailability{" +
                "id=" + id +
                ", room=" + room +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                '}';
    }
}
