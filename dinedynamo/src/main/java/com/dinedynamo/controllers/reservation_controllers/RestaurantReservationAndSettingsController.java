package com.dinedynamo.controllers.reservation_controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.reservation_collections.Reservation;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.collections.reservation_collections.RestaurantReservationSettings;
import com.dinedynamo.dto.table_dtos.ReservationRequestDTO;
import com.dinedynamo.helper.DateTimeUtility;
import com.dinedynamo.repositories.reservation_repositories.ReservationRepository;
import com.dinedynamo.repositories.restaurant_repositories.RestaurantRepository;
import com.dinedynamo.repositories.reservation_repositories.RestaurantReservationSettingsRepository;
import com.dinedynamo.services.reservation_services.RestaurantReservationService;
import com.dinedynamo.services.restaurant_services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    /**
     * @param restaurant
     * @return List of Reservation requests that are on HOLD i.e not accepted by the restaurant owner for his/her restaurant
     */
    @PostMapping("/dinedynamo/restaurant/reservations/get-hold-reservations")
    ResponseEntity<ApiResponse> getAllHoldReservations(@RequestBody Restaurant restaurant){

        boolean isRestaurantPresent = restaurantService.isRestaurantPresentinDb(restaurant.getRestaurantId());
        if(!isRestaurantPresent){
            throw new RuntimeException("RESTAURANT-ID NOT IN DB");
        }

        List<Reservation> holdReservations = reservationRepository.findHoldReservationsByRestaurantId(restaurant.getRestaurantId()).orElse(new ArrayList<>());

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",holdReservations),HttpStatus.OK);


    }

    /**
     * @param restaurant
     * @return List of Reservation requests that have been cancelled by the user
     */
    @PostMapping("/dinedynamo/restaurant/reservations/get-cancelled-reservations")
    ResponseEntity<ApiResponse> getAllCancelledReservations(@RequestBody Restaurant restaurant){

        boolean isRestaurantPresent = restaurantService.isRestaurantPresentinDb(restaurant.getRestaurantId());
        if(!isRestaurantPresent){
            throw new RuntimeException("RESTAURANT-ID NOT IN DB");
        }

        List<Reservation> cancelledReservations = reservationRepository.findCancelledReservationsByRestaurantId(restaurant.getRestaurantId()).orElse(new ArrayList<>());

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",cancelledReservations),HttpStatus.OK);

    }

    /**
     *
     * @param restaurant
     * @return List of Reservation requests that have been ACCEPTED by the restaurant owner for his/her restaurant
     */
    @PostMapping("/dinedynamo/restaurant/reservations/get-accepted-reservations")
    ResponseEntity<ApiResponse> getAllAcceptedReservations(@RequestBody Restaurant restaurant){

        boolean isRestaurantPresent = restaurantService.isRestaurantPresentinDb(restaurant.getRestaurantId());
        if(!isRestaurantPresent){
            throw new RuntimeException("RESTAURANT-ID NOT IN DB");
        }

        List<Reservation> acceptedReservations = reservationRepository.findAcceptedReservationsByRestaurantId(restaurant.getRestaurantId()).orElse(new ArrayList<>());

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",acceptedReservations),HttpStatus.OK);

    }


    /**
     *
     * @param reservationRequestDTO
     * @return Reservation Object
     * When the restaurant accepts the reservation request made by the customer, this API will change the status of reservation request from HOLD to ACCEPTED
     */
    @PostMapping("/dinedynamo/restaurant/reservations/accept-reservation")
    ResponseEntity<ApiResponse> acceptReservation(@RequestBody ReservationRequestDTO reservationRequestDTO){

        Reservation reservation = restaurantReservationService.acceptReservationOfCustomer(reservationRequestDTO.getReservation(),reservationRequestDTO.getMessageContent());

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",reservation),HttpStatus.OK);

    }



    /**
     *
     * @param reservationRequestDTO
     * @return Reservation Object
     * When the restaurant rejects the reservation request made by the customer, this API will change the status of reservation request from HOLD to REJECTED
     */
    @PostMapping("/dinedynamo/restaurant/reservations/reject-reservation")
    ResponseEntity<ApiResponse> rejectReservation(@RequestBody ReservationRequestDTO reservationRequestDTO){

        Reservation reservation = restaurantReservationService.rejectReservationOfCustomer(reservationRequestDTO.getReservation(),reservationRequestDTO.getMessageContent());

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",reservation),HttpStatus.OK);

    }


    /**
     *
     * @param restaurantReservationSettings
     * @return restaurantReservationSettings
     * Use: for adding or updating the Settings related to the reservations
     */
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


    /**
     * @param restaurant
     * @return RestaurantReservationSettings
     * Use: to fetch the reservation settings of a particular restaurant
     */
    @PostMapping("/dinedynamo/restaurant/reservations/get-reservation-settings")
    ResponseEntity<ApiResponse> getRestaurantReservationSettings(@RequestBody Restaurant restaurant){

        RestaurantReservationSettings restaurantReservationSettings = restaurantReservationSettingsRepository.findByRestaurantId(restaurant.getRestaurantId()).orElse(null);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",restaurantReservationSettings),HttpStatus.OK);
    }

    /**
     *
     * @param reservation
     * @return reservation
     * When the customer cancels the reservation, the status of that record will be changed from HOLD/ACCEPTED to CANCELLED in the database
     * Now when the restaurant owner views the cancelled reservations, he/she can delete those reservations from the database using this api
     */
    @PostMapping("/dinedynamo/restaurant/reservations/delete-cancelled-reservations")
    ResponseEntity<ApiResponse> deleteCancelledReservations(@RequestBody Reservation reservation){

        Reservation deletedReservation = restaurantReservationService.deleteCancelledReservation(reservation);

        if(deletedReservation == null){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND,"success",reservation),HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",deletedReservation),HttpStatus.OK);
        }

    }

    /**
     *
     * @param restaurant
     * @return boolean
     * This api needs to be hit periodically
     * This api will delete all the records from 'reservations' collection which have their dineInDateAndTime before the current date and time
     */
    @PostMapping("/dinedynamo/restaurant/reservations/clear-old-reservations")
    ResponseEntity<ApiResponse> clearOldReservations(@RequestBody Restaurant restaurant){

        boolean isDeleted = restaurantReservationService.clearOldReservationsFromDb(restaurant.getRestaurantId());

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",isDeleted),HttpStatus.OK);

    }



}
