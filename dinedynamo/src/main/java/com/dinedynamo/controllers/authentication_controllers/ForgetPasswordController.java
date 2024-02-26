package com.dinedynamo.controllers.authentication_controllers;

import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.authentication_collections.ResetPasswordRequest;
import com.dinedynamo.collections.authentication_collections.Restaurant;
import com.dinedynamo.repositories.RestaurantRepository;
import com.dinedynamo.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

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

            // Generate a unique token for password reset
            String resetToken = UUID.randomUUID().toString();
            existingRestaurant.setResetToken(resetToken);
            restaurantRepository.save(existingRestaurant);

            // Send email with reset link
            String resetLink = "http://localhost:5173/set-password?token=" + resetToken;
            emailService.sendPasswordResetEmail(email, resetLink);

            ApiResponse response = new ApiResponse(HttpStatus.OK, "success", "Password reset link has been sent to your email");
            return ResponseEntity.ok(response);
        } else {
            ApiResponse response = new ApiResponse(HttpStatus.NOT_FOUND, "failure", "No restaurant found with this email");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }



    @PostMapping("/dinedynamo/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody ResetPasswordRequest requestBody) {
        String resetToken = requestBody.getResetToken();
        String newPassword = requestBody.getNewPassword();

        Restaurant existingRestaurant = restaurantRepository.findByResetToken(resetToken);
        if (existingRestaurant != null) {
            existingRestaurant.setRestaurantPassword(passwordEncoder.encode(newPassword));

            existingRestaurant.setResetToken(null);
            restaurantRepository.save(existingRestaurant);
            ApiResponse response = new ApiResponse(HttpStatus.OK, "success", "Password reset successfully");
            return ResponseEntity.ok(response);
        } else {
            ApiResponse response = new ApiResponse(HttpStatus.NOT_FOUND, "failure", "Invalid or expired reset token");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }



    //    @PostMapping("/dinedynamo/forget-password")
//    public ResponseEntity<ApiResponse> forgetPassword(@RequestBody Restaurant restaurant, HttpServletRequest request) {
//        String email = restaurant.getRestaurantEmail();
//        Optional<Restaurant> optionalRestaurant = restaurantRepository.findByRestaurantEmail(email);
//        if (optionalRestaurant.isPresent()) {
//            Restaurant existingRestaurant = optionalRestaurant.get();
//
//            // Generate a unique token for password reset
//            String resetToken = UUID.randomUUID().toString();
//
//            // Store token and email in session
//            HttpSession session = request.getSession();
//            session.setAttribute("resetToken", resetToken);
//            session.setAttribute("email", email);
//
//            // Send email with reset link
//            String resetLink = "http://localhost:5173/reset-password";
//            emailService.sendPasswordResetEmail(email, resetLink);
//
//            ApiResponse response = new ApiResponse(HttpStatus.OK, "success", "Password reset link has been sent to your email");
//            return ResponseEntity.ok(response);
//        } else {
//            ApiResponse response = new ApiResponse(HttpStatus.NOT_FOUND, "failure", "No restaurant found with this email");
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//        }
//    }



}
