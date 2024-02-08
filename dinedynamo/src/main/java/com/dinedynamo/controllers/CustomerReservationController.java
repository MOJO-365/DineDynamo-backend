package com.dinedynamo.controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.Reservation;
import com.dinedynamo.dto.GetPreviousReservationDTO;
import com.dinedynamo.repositories.ReservationRepository;
import com.dinedynamo.services.CustomerReservationService;
import com.dinedynamo.services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
public class CustomerReservationController
{
    @Autowired
    CustomerReservationService customerReservationService;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    RestaurantService restaurantService;

    @PostMapping("/dinedynamo/customer/reservations/request-reseration")
    ResponseEntity<ApiResponse> requestReservation(@RequestBody Reservation reservation){

        boolean isRequestValid = customerReservationService.validateReservationRequest(reservation);

        if(!isRequestValid){

            System.out.println("RESERVATION REQUEST DOES NOT CONTAIN ALL REQUIRED FIELDS");
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_ACCEPTABLE,"success",null),HttpStatus.OK);
        }

        reservation = customerReservationService.save(reservation);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",reservation),HttpStatus.OK);
    }


    @PostMapping("/dinedynamo/customer/reservations/cancel-reseration")
    ResponseEntity<ApiResponse> cancelReservation(@RequestBody Reservation reservation){

        boolean isRequestValid = customerReservationService.validateReservationRequest(reservation);

        if(!isRequestValid){

            System.out.println("RESERVATION REQUEST DOES NOT CONTAIN ALL REQUIRED FIELDS");
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_ACCEPTABLE,"success",null),HttpStatus.OK);
        }


        reservationRepository.delete(reservation);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",reservation),HttpStatus.OK);

    }

    @PostMapping("/dinedynamo/customer/reservations/get-previous-reservations")
    ResponseEntity<ApiResponse> getPreviousReservations(@RequestBody GetPreviousReservationDTO getPreviousReservationDTO){


        boolean isRestaurantPresent = restaurantService.isRestaurantPresentinDb(getPreviousReservationDTO.getRestaurantId());

        if(!isRestaurantPresent){
            System.out.println("RESTAURANT-ID NOT FOUND IN DB");
            throw new RuntimeException("RESTAURANT-ID NOT FOUND IN DB");
        }
        List<Reservation> holdReservations = reservationRepository.findHoldReservationsByRestaurantIdAndCustomerPhone(getPreviousReservationDTO.getRestaurantId(), getPreviousReservationDTO.getCustomerPhone()).orElse(null);

        List<Reservation> acceptedReservations = reservationRepository.findAcceptedReservationsByRestaurantIdAndCustomerPhone(getPreviousReservationDTO.getRestaurantId(), getPreviousReservationDTO.getCustomerPhone()).orElse(null);

        List<Reservation> rejectedReservations = reservationRepository.findRejectedReservationsByRestaurantIdAndCustomerPhone(getPreviousReservationDTO.getRestaurantId(), getPreviousReservationDTO.getCustomerPhone()).orElse(null);

        List<List<Reservation>> listOfAllReservations = new ArrayList<>();

        if(acceptedReservations.isEmpty() && holdReservations.isEmpty() && rejectedReservations.isEmpty()){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",null),HttpStatus.OK);

        }
        listOfAllReservations.add(holdReservations);
        listOfAllReservations.add(acceptedReservations);
        listOfAllReservations.add(rejectedReservations);


        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",listOfAllReservations),HttpStatus.OK);



    }
}
