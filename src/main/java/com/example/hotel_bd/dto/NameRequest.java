package com.example.hotel_bd.dto;

import lombok.Data;

@Data
public class NameRequest {
    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
