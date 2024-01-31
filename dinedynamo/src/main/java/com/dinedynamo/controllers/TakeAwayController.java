package com.dinedynamo.controllers;

import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.DeliveryOrder;
import com.dinedynamo.collections.Restaurant;
import com.dinedynamo.collections.TakeAway;
import com.dinedynamo.repositories.RestaurantRepository;
import com.dinedynamo.repositories.TakeAwayOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class TakeAwayController {

    @Autowired
    private TakeAwayOrderRepository takeAwayRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;


    @PostMapping("/dinedynamo/restaurant/orders/takeaway")
    public ResponseEntity<ApiResponse> createTakeAwayOrder(@RequestBody TakeAway takeAway) {
            TakeAway savedOrder = takeAwayRepository.save(takeAway);
            ApiResponse response = new ApiResponse(HttpStatus.OK, "success", savedOrder);
            return new ResponseEntity<>(response, HttpStatus.OK);

    }


    @PostMapping("/dinedynamo/restaurant/orders/getTakeAway")
    public ResponseEntity<ApiResponse> getAllOrders(@RequestBody Restaurant restaurant){


        restaurant = restaurantRepository.findById(restaurant.getRestaurantId()).orElse(null);


        if (restaurant == null)
        {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "success", null), HttpStatus.NOT_FOUND);
        }
        else
        {
            List<TakeAway> orders = takeAwayRepository.findByRestaurantId(restaurant.getRestaurantId());

            return new ResponseEntity<ApiResponse>(new ApiResponse(HttpStatus.OK, "success", orders), HttpStatus.OK);

        }
    }


}