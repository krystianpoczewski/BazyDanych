package com.example.hotel_bd.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

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
