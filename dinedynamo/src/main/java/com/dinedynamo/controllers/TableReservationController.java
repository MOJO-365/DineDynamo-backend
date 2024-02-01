package com.dinedynamo.controllers;


import com.cloudinary.Api;
import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.Reservation;
import com.dinedynamo.repositories.TableReservationRepository;
import com.dinedynamo.services.TableReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class TableReservationController
{

    @Autowired
    TableReservationRepository tableReservationRepository;

    @Autowired
    TableReservationService tableReservationService;


    /**
     *
     * @param reservation
     * @return ApiResponse.data will have true if table is reserved else false
     */
    @PostMapping("/dinedynamo/customer/reserve-table")
    ResponseEntity<ApiResponse> reserveTable(@RequestBody Reservation reservation){

        boolean isReservationPossible = tableReservationService.save(reservation);

        System.out.println("IS TABLE RESERVED: "+isReservationPossible);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",isReservationPossible),HttpStatus.OK);

    }

    @PostMapping("dinedynamo/customer/unreserve-table")
    ResponseEntity<ApiResponse> unreserveTable(@RequestBody Reservation reservation){
        String reservationId = reservation.getReservationId();

        if(reservationId.equals(" ") || reservationId.equals("") || reservationId == null){
            System.out.println("RESERVATION-ID IS EMPTY OR NULL IN REQUEST");
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",null),HttpStatus.OK);

        }

        tableReservationRepository.delete(reservation);
        System.out.println("RESERVATION DELETED FROM DB");
        return new ResponseEntity<ApiResponse>(new ApiResponse(HttpStatus.OK,"success",reservation),HttpStatus.OK);

    }
}
