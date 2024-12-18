package com.example.hotel_bd.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Represents a reservation made by a user, including details about the check-in
 * and check-out dates, rooms, and optional amenities associated with the reservation.
 *
 * The Reservation class is annotated as an entity, meaning it is mapped to a database table.
 * It uses several Java persistence annotations to define relationships and constraints.
 *
 * The class provides methods to calculate the price of the reservation, manage associated
 * rooms and amenities, and access or modify various reservation details.
 */
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(name = "check_in_date")
    private Date checkInDate;

    @NotNull
    @Column(name = "check_out_date")
    private Date checkOutDate;

    @NotNull
    @ManyToMany
    @JoinTable(name = "reservation_roomsJT",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id"))
    private List<Room> rooms;

    @ManyToMany
    @JoinTable(name = "reservation_amenitiesJT",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id"))
    private List<ReservationAmenities> amenities;

    public BigDecimal calculatePrice() {
        if (rooms == null || rooms.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (Room room : rooms) {
            BigDecimal roomPrice = room.getPricePerNight() != null ? room.getPricePerNight() : BigDecimal.ZERO;
            totalPrice = totalPrice.add(roomPrice);
        }
        if (amenities != null && !amenities.isEmpty()) {
            for (ReservationAmenities amenity : amenities) {
                BigDecimal amenityPrice = amenity.getPricePerNight() != null ? amenity.getPricePerNight() : BigDecimal.ZERO;
                totalPrice = totalPrice.add(amenityPrice);
            }
        }

        long durationInMillis = checkOutDate.getTime() - checkInDate.getTime();
        long totalDays = TimeUnit.MILLISECONDS.toDays(durationInMillis);

        if (totalDays <= 0) {
            return BigDecimal.ZERO;
        }

        return totalPrice.divide(BigDecimal.valueOf(totalDays), 2, RoundingMode.HALF_UP);
    }


    public Reservation() {}

    public Reservation(User user, Date checkInDate, Date checkOutDate, List<Room> rooms, List<ReservationAmenities> amenities) {
        this.user = user;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.rooms = rooms;
        this.amenities = amenities;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<ReservationAmenities> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<ReservationAmenities> amenities) {
        this.amenities = amenities;
    }

    public void addAmenity(ReservationAmenities amenity) {
        amenities.add(amenity);
    }

    public void removeAmenity(ReservationAmenities amenity) {
        amenities.remove(amenity);
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public void removeRoom(Room room) {
        if (rooms.size() > 1) {
            rooms.remove(room);
        }
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", user=" + user +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", rooms=" + rooms +
                ", amenities=" + amenities +
                '}';
    }
}
