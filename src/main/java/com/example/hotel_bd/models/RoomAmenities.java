package com.example.hotel_bd.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * The RoomAmenities class represents the amenities that can be associated with a room
 * in a hotel management system. Each amenity has a unique identifier and a name.
 * This class is annotated as an entity to be used with Java Persistence API (JPA) for database operations.
 *
 * Fields:
 * - id: A unique identifier for the RoomAmenities entity, which is automatically generated.
 * - name: The name of the amenity, which must be a non-null string with a length between 1 and 50 characters.
 *
 * Key Behaviors:
 * - Provides getter and setter methods for the id and name properties.
 * - Overrides the toString method to return a string representation of the RoomAmenities object.
 */
@Entity
public class RoomAmenities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_ammenities_id")
    private Integer id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(unique = true, name = "name")
    private String name;

    public RoomAmenities() {}

    public RoomAmenities(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @NotNull @Size(min = 1, max = 50) String getName() {
        return name;
    }

    public void setName(@NotNull @Size(min = 1, max = 50) String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "RoomAmmenities{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
