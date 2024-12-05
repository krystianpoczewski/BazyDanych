package com.example.hotel_bd.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
