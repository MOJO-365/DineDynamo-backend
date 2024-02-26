package com.dinedynamo.controllers.old_orders_controllers;

import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.collections.old_order_collections.TakeAway;
import com.dinedynamo.repositories.invoice_repositories.FinalBillRepository;
import com.dinedynamo.repositories.restaurant_repositories.RestaurantRepository;
import com.dinedynamo.repositories.old_orders_repositories.TakeAwayOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin("*")
public class TakeAwayController {

    @Autowired
    private TakeAwayOrderRepository takeAwayRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private FinalBillRepository finalBillRepository;




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




    @DeleteMapping("/dinedynamo/restaurant/takeaway/deleteorder")
    public ResponseEntity<ApiResponse> deleteOrder(@RequestBody TakeAway takeAway) {
        Optional<TakeAway> deleteOrder = takeAwayRepository.findById(takeAway.getTakeAwayId());

        if (deleteOrder.isPresent()) {
            takeAwayRepository.deleteById(takeAway.getTakeAwayId());
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", null), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "failure", null), HttpStatus.NOT_FOUND);
        }
    }







}
