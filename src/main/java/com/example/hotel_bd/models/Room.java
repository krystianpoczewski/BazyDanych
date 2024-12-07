package com.example.hotel_bd.models;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;


/**
 * Represents a Room entity with an identifier, type, capacity, price per night, and associated amenities.
 *
 * The Room can have a defined room type, a specified number of people it can accommodate,
 * a nightly rate, and a collection of amenities that enhance the room's offerings.
 *
 * Attributes:
 * - id: A unique identifier for the Room, automatically generated.
 * - type: The type of Room, as defined by the RoomType entity.
 * - capacity: An integer representing the maximum number of occupants the Room can accommodate.
 * - pricePerNight: The cost of renting the Room for one night.
 * - amenities: A list of available amenities in the Room.
 */
@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomType type;

    @NotNull
    @Min(1)
    @Max(8)
    @Column(name = "capacity")
    private Integer capacity;

    @NotNull
    @Column(name = "price_per_night")
    private BigDecimal pricePerNight;

    @NotNull
    @ManyToMany
    @JoinTable(
            name = "room_amenitiesJT",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    private List<RoomAmenities> amenities;

    public Room() {
    }

    public Room(RoomType type, Integer capacity, BigDecimal pricePerNight, List<RoomAmenities> amenities) {
        this.type = type;
        this.capacity = capacity;
        this.pricePerNight = pricePerNight;
        this.amenities = amenities;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public List<RoomAmenities> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<RoomAmenities> amenities) {
        this.amenities = amenities;
    }

    public void addAmenity(RoomAmenities amenity) {
        amenities.add(amenity);
    }

    public void removeAmenity(RoomAmenities amenity) {
        amenities.remove(amenity);
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", type=" + type +
                ", capacity=" + capacity +
                ", pricePerNight=" + pricePerNight +
                ", amenities=" + amenities +
                '}';
    }
}
