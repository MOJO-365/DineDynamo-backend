package com.dinedynamo.controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.Reservation;
import com.dinedynamo.collections.Restaurant;
import com.dinedynamo.collections.RestaurantReservationSettings;
import com.dinedynamo.dto.ReservationRequestDTO;
import com.dinedynamo.helper.DateTimeUtility;
import com.dinedynamo.repositories.ReservationRepository;
import com.dinedynamo.repositories.RestaurantRepository;
import com.dinedynamo.repositories.RestaurantReservationSettingsRepository;
import com.dinedynamo.services.RestaurantReservationService;
import com.dinedynamo.services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dinedynamo.services.RestaurantReservationService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    DateTimeUtility dateTimeUtility;

    @Autowired
    RestaurantReservationSettingsRepository restaurantReservationSettingsRepository;

    @Autowired
    RestaurantReservationService restaurantReservationSettingsService;

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


    @PostMapping("/dinedynamo/restaurant/reservations/add-or-edit-reservation-settings")
    ResponseEntity<ApiResponse> editReservationSettings(@RequestBody RestaurantReservationSettings restaurantReservationSettings){


        String restaurantId = restaurantReservationSettings.getRestaurantId();

        RestaurantReservationSettings existingRestaurantReservationSettings = restaurantReservationSettingsRepository.findByRestaurantId(restaurantId).orElse(null);

        if(existingRestaurantReservationSettings != null){

            restaurantReservationSettings.setReservationSettingsId(existingRestaurantReservationSettings.getReservationSettingsId());

            restaurantReservationSettingsRepository.delete(existingRestaurantReservationSettings);
            restaurantReservationSettingsRepository.save(restaurantReservationSettings);
        }

        else{
            restaurantReservationSettingsRepository.save(restaurantReservationSettings);
        }

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",restaurantReservationSettings),HttpStatus.OK);

    }


    @PostMapping("/dinedynamo/restaurant/reservations/get-reservation-settings")
    ResponseEntity<ApiResponse> getRestaurantReservationSettings(@RequestBody Restaurant restaurant){

        RestaurantReservationSettings restaurantReservationSettings = restaurantReservationSettingsRepository.findByRestaurantId(restaurant.getRestaurantId()).orElse(null);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",restaurantReservationSettings),HttpStatus.OK);
    }



    @PostMapping("/dinedynamo/restaurant/reservations/clear-old-reservations")
    ResponseEntity<ApiResponse> clearOldREservations(@RequestBody Restaurant restaurant){

        boolean isDeleted = restaurantReservationService.clearOldReservationsFromDb(restaurant.getRestaurantId());

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",isDeleted),HttpStatus.OK);

    }
}
