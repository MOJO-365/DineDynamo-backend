package com.dinedynamo.controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.Restaurant;
import com.dinedynamo.collections.WaitingList;
import com.dinedynamo.dto.ReservationOrWaitingResponseBody;
import com.dinedynamo.repositories.RestaurantRepository;
import com.dinedynamo.repositories.WaitingListRepository;
import com.dinedynamo.services.RestaurantService;
import com.dinedynamo.services.WaitingListService;
import jdk.javadoc.doclet.Reporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
public class WaitingListController {

    @Autowired
    WaitingListRepository waitingListRepository;

    @Autowired
    WaitingListService waitingListService;

    @Autowired
    RestaurantService restaurantService;

    @PostMapping("/dinedynamo/customer/waitings/add-to-waiting")
    ResponseEntity<ApiResponse> addToWaitingList(@RequestBody WaitingList waitingList){

        boolean isWaitingListRequestValid = waitingListService.isWaitingListRequestValid(waitingList);

        if(!isWaitingListRequestValid){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_ACCEPTABLE,"success",new ReservationOrWaitingResponseBody(null,"INVALID_REQUEST")),HttpStatus.OK);
        }


        boolean isSavedInWaitingList = waitingListService.save(waitingList);

        if(!isSavedInWaitingList){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND,"success",new ReservationOrWaitingResponseBody(waitingList,"WAITING")),HttpStatus.OK);
        }


        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",waitingList),HttpStatus.OK);

    }


    @PostMapping("/dinedynamo/restaurant/waitings/get-all-sorted-waitings")
    ResponseEntity<ApiResponse> getAllWaitingsOfRestaurantSortedByReservationDateAndTime(@RequestBody Restaurant restaurant){

        String restaurantId = restaurant.getRestaurantId();

        if(restaurantId.equals("") || restaurantId.equals(" ") || restaurantId == null){
            System.out.println("RESTAURANT-ID IS EMPTY OR NULL");
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_ACCEPTABLE,"success",null),HttpStatus.OK);
        }

        boolean doesRestaurantExist = restaurantService.isRestaurantPresentinDb(restaurantId);

        if(!doesRestaurantExist){
            System.out.println("RESTAURANT-ID IS NOT PRESENT IN DB");
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_ACCEPTABLE,"success",null),HttpStatus.OK);
        }

        List<WaitingList> waitings = waitingListRepository.findByRestaurantId(restaurantId).orElse(new ArrayList<>());


        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",waitings),HttpStatus.OK);
    }



    @DeleteMapping("/dinedynamo/restaurant/waitings/delete-waiting")
    ResponseEntity<ApiResponse> deleteWaitingListFromDB(@RequestBody WaitingList waitingList){

        String waitingListId = waitingList.getWaitingId();

        if(waitingListId.equals(" ") || waitingListId.equals("") || waitingListId == null){
            System.out.println("WAITING-LIST-ID NOT FOUND IN REQUEST");
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_ACCEPTABLE,"success",new ReservationOrWaitingResponseBody(waitingList,"INVALID_REQUEST")),HttpStatus.OK);

        }

        waitingList = waitingListRepository.findById(waitingListId).orElse(null);

        if(waitingList == null){
            System.out.println("WAITING-LIST-ID NOT FOUND IN DB");
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND,"success",new ReservationOrWaitingResponseBody(waitingList,"INVALID_REQUEST")),HttpStatus.OK);
        }

        waitingListRepository.delete(waitingList);

        return new ResponseEntity<ApiResponse>(new ApiResponse(HttpStatus.OK,"success",new ReservationOrWaitingResponseBody(waitingList,"WAITING_DELETED")),HttpStatus.OK);

    }



}
