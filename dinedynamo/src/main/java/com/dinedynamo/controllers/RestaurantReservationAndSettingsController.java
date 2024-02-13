package com.dinedynamo.controllers;


import com.cloudinary.Api;
import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.Reservation;
import com.dinedynamo.collections.Restaurant;
import com.dinedynamo.collections.RestaurantReservationSettings;
import com.dinedynamo.dto.ReservationRequestDTO;
import com.dinedynamo.dto.RestaurantReservationSettingsDTO;
import com.dinedynamo.repositories.ReservationRepository;
import com.dinedynamo.repositories.RestaurantRepository;
import com.dinedynamo.repositories.RestaurantReservationSettingsRepository;
import com.dinedynamo.services.RestaurantReservationService;
import com.dinedynamo.services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RestController
@CrossOrigin("*")
public class RestaurantReservationAndSettingsController
{
    @Autowired
    RestaurantReservationService restaurantReservationService;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    RestaurantReservationSettingsRepository restaurantReservationSettingsRepository;

    @PostMapping("/dinedynamo/restaurant/reservations/get-hold-reservations")
    ResponseEntity<ApiResponse> getAllHoldReservations(@RequestBody Restaurant restaurant){

        boolean isRestaurantPresent = restaurantService.isRestaurantPresentinDb(restaurant.getRestaurantId());
        if(!isRestaurantPresent){
            throw new RuntimeException("RESTAURANT-ID NOT IN DB");
        }

        List<Reservation> holdReservations = reservationRepository.findHoldReservationsByRestaurantId(restaurant.getRestaurantId()).orElse(new ArrayList<>());

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",holdReservations),HttpStatus.OK);


    }


    @PostMapping("/dinedynamo/restaurant/reservations/get-accepted-reservations")
    ResponseEntity<ApiResponse> getAllAcceptedReservations(@RequestBody Restaurant restaurant){

        boolean isRestaurantPresent = restaurantService.isRestaurantPresentinDb(restaurant.getRestaurantId());
        if(!isRestaurantPresent){
            throw new RuntimeException("RESTAURANT-ID NOT IN DB");
        }

        List<Reservation> acceptedReservations = reservationRepository.findAcceptedReservationsByRestaurantId(restaurant.getRestaurantId()).orElse(new ArrayList<>());

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",acceptedReservations),HttpStatus.OK);

    }



    @PostMapping("/dinedynamo/restaurant/reservations/accept-reservation")
    ResponseEntity<ApiResponse> acceptReservation(@RequestBody ReservationRequestDTO reservationRequestDTO){

        Reservation reservation = restaurantReservationService.acceptReservationOfCustomer(reservationRequestDTO.getReservation(),reservationRequestDTO.getMessageContent());

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",reservation),HttpStatus.OK);

    }


    @PostMapping("/dinedynamo/restaurant/reservations/reject-reservation")
    ResponseEntity<ApiResponse> rejectReservation(@RequestBody ReservationRequestDTO reservationRequestDTO){

        Reservation reservation = restaurantReservationService.rejectReservationOfCustomer(reservationRequestDTO.getReservation(),reservationRequestDTO.getMessageContent());

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",reservation),HttpStatus.OK);

    }



    @PostMapping("/dinedynamo/restaurant/reservations/test")
    String test(){

        Restaurant restaurant = restaurantRepository.findById("65b88c1ff6d0de4c234f4877").orElse(null);

        String dateString = "12/15/2024, 5:10:00 PM";

        // Define the DateTimeFormatter with the appropriate pattern and US locale
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy, h:mm:ss a", Locale.US);

        // Parse the string to LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.parse(dateString, formatter);


        System.out.println("Converted LocalDateTime: " + localDateTime.toLocalTime());


        return "done";

    }


    @PostMapping("/dinedynamo/restaurant/reservations/add-reservation-settings")
    ResponseEntity<ApiResponse> addReservationSettings(@RequestBody RestaurantReservationSettings restaurantReservationSettings){



        restaurantReservationSettingsRepository.save(restaurantReservationSettings);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",restaurantReservationSettings),HttpStatus.OK);

    }

    @PutMapping("/dinedynamo/restaurant/reservations/edit-reservation-settings")
    ResponseEntity<ApiResponse> editReservationSettings(@RequestBody RestaurantReservationSettingsDTO restaurantReservationSettingsDTO){


        RestaurantReservationSettings restaurantReservationSettings = restaurantReservationSettingsDTO.getRestaurantReservationSettings();

        restaurantReservationSettings.setRestaurantId(restaurantReservationSettingsDTO.getRestaurantReservationSettingsId());
        restaurantReservationSettingsRepository.save(restaurantReservationSettings);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",restaurantReservationSettings),HttpStatus.OK);

    }



}
