package com.dinedynamo.controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.Reservation;
import com.dinedynamo.collections.Restaurant;
import com.dinedynamo.collections.RestaurantReservationSettings;
import com.dinedynamo.dto.GetPreviousReservationDTO;
import com.dinedynamo.repositories.ReservationRepository;
import com.dinedynamo.repositories.RestaurantReservationSettingsRepository;
import com.dinedynamo.services.CustomerReservationService;
import com.dinedynamo.services.RestaurantService;
import org.bson.Document;
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

    @Autowired
    RestaurantReservationSettingsRepository restaurantReservationSettingsRepository;


    /**
     *
     * @param reservation
     * @return reservation
     * Use: when customer wants to book a table, this api will save the request and once the restaurant owner accepts this, its status will be updated from HOLD to ACCEPTED
     */
    @PostMapping("/dinedynamo/customer/reservations/request-reseration")
    ResponseEntity<ApiResponse> requestReservation(@RequestBody Reservation reservation){

        boolean isRequestValid = customerReservationService.validateReservationRequest(reservation);

        if(!isRequestValid){

            System.out.println("RESERVATION REQUEST DOES NOT CONTAIN ALL REQUIRED FIELDS");
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_ACCEPTABLE,"success",null),HttpStatus.OK);
        }

        reservation = customerReservationService.save(reservation);

        if(reservation.getReservationRequestStatus() == Reservation.ReservationRequestStatus.INVALID){

            System.out.println("DATA NOT SAVED IN DB, AS CUSTOMER ALREADY HAS A BOOKING IN SAME SLOT");
        }

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",reservation),HttpStatus.OK);
    }


    /**
     *
     * @param reservation
     * @return reservation
     * Use: when customer wants to cancel the reservation. The reservation will be deleted from the database
     */
    @PostMapping("/dinedynamo/customer/reservations/cancel-reservation")
    ResponseEntity<ApiResponse> cancelReservation(@RequestBody Reservation reservation){

        boolean isRequestValid = customerReservationService.validateReservationRequest(reservation);

        if(!isRequestValid){

            System.out.println("RESERVATION REQUEST DOES NOT CONTAIN ALL REQUIRED FIELDS");
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_ACCEPTABLE,"success",null),HttpStatus.OK);
        }

        if(reservation.getReservationId() == null || reservation.getReservationId() == ""){
            throw new RuntimeException("Reservation settings id not present in cancel reservation request");
        }

        reservation.setReservationRequestStatus(Reservation.ReservationRequestStatus.CANCELED);
        reservationRepository.save(reservation);
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


    @PostMapping("/dinedynamo/customer/reservation/fetch-reservation-settings")
    ResponseEntity<ApiResponse> getRestaurantReservationSettings(@RequestBody Restaurant restaurant){

        boolean isRestaurantPresentInDB = restaurantService.isRestaurantPresentinDb(restaurant.getRestaurantId());

        if(isRestaurantPresentInDB){

            System.out.println("RESTAURANT-ID NOT FOUND IN DB");
            throw new RuntimeException("restauarntId not found in database");
        }

        RestaurantReservationSettings restaurantReservationSettings = restaurantReservationSettingsRepository.findByRestaurantId(restaurant.getRestaurantId()).orElse(null);

        if(restaurantReservationSettings == null){
            System.out.println("SETTINGS EMPTY, YET TO BE ADDED IN DB BY RESTAURANT ");

        }

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",restaurantReservationSettings),HttpStatus.OK);

    }

}
