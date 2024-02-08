package com.dinedynamo.controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.Reservation;
import com.dinedynamo.collections.Restaurant;
import com.dinedynamo.dto.ReservationRequestDTO;
import com.dinedynamo.repositories.ReservationRepository;
import com.dinedynamo.services.RestaurantReservationService;
import com.dinedynamo.services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
public class RestaurantReservationController
{
    @Autowired
    RestaurantReservationService restaurantReservationService;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    RestaurantService restaurantService;


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




}
