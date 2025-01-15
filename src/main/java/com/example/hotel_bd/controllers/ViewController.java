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

    @GetMapping("/change-password")
    public String ChangePasswordPage(Model model){
        model.addAttribute("title", "Hotel Bd");
        return "changePassword";
    }

    @GetMapping("/admin")
    public String AdminPage(Model model){
        model.addAttribute("title", "Hotel Bd");
        return "admin";
    }

    @GetMapping("/admin/reviews")
    public String AdminReviewPage(Model model){
        model.addAttribute("title", "Hotel Bd");
        return "adminReviews";
    }

    @GetMapping("/admin/reservation-amenities")
    public String AdminReservationAmenitiesPage(Model model){
        model.addAttribute("title", "Hotel Bd");
        return "adminReservationAmenities";
    }
    @GetMapping("/admin/room-type")
    public String AdminRoomTypePage(Model model){
        model.addAttribute("title", "Hotel Bd");
        return "adminRoomType";
    }
    @GetMapping("/admin/room-amenities")
    public String AdminRoomAmenityPage(Model model){
        model.addAttribute("title", "Hotel Bd");
        return "adminRoomAmenities";
    }
    @GetMapping("/admin/room")
    public String AdminRoomPage(Model model){
        model.addAttribute("title", "Hotel Bd");
        return "adminRoom";
    }
    @GetMapping("/admin/reservation")
    public String AdminReservationPage(Model model){
        model.addAttribute("title", "Hotel Bd");
        return "adminReservations";
    }
    @GetMapping("/admin/users")
    public String AdminUserPage(Model model){
        model.addAttribute("title", "Hotel Bd");
        return "adminUser";
    }
}
