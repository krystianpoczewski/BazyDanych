package com.example.hotel_bd.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    @GetMapping("/home")
    public String HomePage(Model model){
        model.addAttribute("title", "Hotel Bd");
        return "index";
    }

    @GetMapping("/login")
    public String LoginPage(Model model){
        model.addAttribute("title", "Hotel Bd");
        return "login";
    }

    @GetMapping("/register")
    public String RegisterPage(Model model){
        model.addAttribute("title", "Hotel Bd");
        return "register";
    }

    @GetMapping("/reservations")
    public String ReservationsPage(Model model){
        model.addAttribute("title", "Hotel Bd");
        return "reservations";
    }

    @GetMapping("/rooms")
    public String RoomsPage(Model model){
        model.addAttribute("title", "Hotel Bd");
        return "rooms";
    }

    @GetMapping("/reviews")
    public String ReviewsPage(Model model){
        model.addAttribute("title", "Hotel Bd");
        return "reviews";
    }

    @GetMapping("/admin")
    public String AdminPage(Model model){
        model.addAttribute("title", "Hotel Bd");
        return "admin";
    }
}
