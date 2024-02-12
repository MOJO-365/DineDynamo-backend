package com.dinedynamo.controllers;

import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.Restaurant;
import com.dinedynamo.repositories.RestaurantRepository;
import com.dinedynamo.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Random;

@RestController
@CrossOrigin("*")
public class ForgetPasswordController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @PostMapping("/dinedynamo/forget-password")
    public ResponseEntity<ApiResponse> forgetPassword(@RequestBody Restaurant restaurant) {
        String email = restaurant.getRestaurantEmail();

        Optional<Restaurant> optionalRestaurant = restaurantRepository.findByRestaurantEmail(email);
        if (optionalRestaurant.isPresent()) {
            Restaurant existingRestaurant = optionalRestaurant.get();
            String newPassword = generateRandomPassword();

            existingRestaurant.setRestaurantPassword(passwordEncoder.encode(newPassword));
            restaurantRepository.save(existingRestaurant);

            emailService.sendNewPasswordEmail(existingRestaurant.getRestaurantEmail(), newPassword, existingRestaurant.getRestaurantName());

            ApiResponse response = new ApiResponse(HttpStatus.OK, "success", "A new password has been sent to your email");
            return ResponseEntity.ok(response);
        } else {
            ApiResponse response = new ApiResponse(HttpStatus.NOT_FOUND, "failure", "No restaurant found with this email");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    private String generateRandomPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder newPassword = new StringBuilder();
        Random rnd = new Random();
        while (newPassword.length() < 8) {
            int index = (int) (rnd.nextFloat() * characters.length());
            newPassword.append(characters.charAt(index));
        }
        return newPassword.toString();
    }
}
