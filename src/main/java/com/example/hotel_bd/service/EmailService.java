package com.example.hotel_bd.service;


import com.example.hotel_bd.models.Reservation;
import com.example.hotel_bd.models.User;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Locale;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(message);

            System.out.println("Email sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
    public String getEmailMessage(User user, Reservation reservation) {
        if (user == null || reservation == null) {
            return "Invalid reservation details.";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String checkInFormatted = dateFormat.format(reservation.getCheckInDate());
        String checkOutFormatted = dateFormat.format(reservation.getCheckOutDate());

        String price = String.format("%.2f", reservation.calculatePrice());

        return String.format("YOU CREATED A RESERVATION!\n" +
                "FROM: %s\n" +
                "TO: %s\n" +
                "IT COSTS: $%s", checkInFormatted, checkOutFormatted, price);
    }
}
