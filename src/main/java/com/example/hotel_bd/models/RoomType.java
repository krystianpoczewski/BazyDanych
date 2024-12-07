package com.example.hotel_bd.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Represents a type of room in a hotel or similar establishment.
 * This entity contains an identifier and a unique name for the room type.
 *
 * An instance of RoomType can be persisted in a database and retrieved
 * as needed for describing the types of rooms available.
 *
 * The 'id' field is an auto-generated unique identifier for each room type
 * in the database. The 'name' field is a string that represents the name
 * of the room type, which must be unique and between 1 and 50 characters.
 *
 * This class includes methods to retrieve and modify these fields,
 * as well as a default constructor and a parameterized constructor for creating
 * instances with a specified name.
 *
 * The class provides standard getter and setter methods for its fields,
 * and overrides the toString method to return a string representation of
 * the room type, including its id and name.
 *
 * The uniqueness of the 'name' field is ensured with a database constraint,
 * and the field is annotated to enforce non-null values and size constraints
 * at the application level.
 *
 * Intended usage involves creating instances of RoomType to represent
 * the different room categories available in an accommodation facility.
 */
@Entity
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_type_id")
    private Integer id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(unique = true, name = "name")
    private String name;

    public RoomType() {}

    public RoomType(String name) {
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
        return "RoomType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
