package com.dinedynamo.controllers;

import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.Restaurant;
import com.dinedynamo.collections.TakeAway;
import com.dinedynamo.repositories.RestaurantRepository;
import com.dinedynamo.repositories.TakeAwayOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
public class NewTakeAwayController {

    @Autowired
    private TakeAwayOrderRepository takeAwayRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @PostMapping("/dinedynamo/restaurant/takeaway/place")
    public ResponseEntity<ApiResponse> createTakeAwayOrder(@RequestBody TakeAway takeAway) {
        TakeAway savedOrder = takeAwayRepository.save(takeAway);
        ApiResponse response = new ApiResponse(HttpStatus.OK, "Success", savedOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/dinedynamo/restaurant/takeaway/all")
    public ResponseEntity<ApiResponse> getAllTakeAwayOrders(@RequestBody Restaurant restaurant) {
        restaurant = restaurantRepository.findById(restaurant.getRestaurantId()).orElse(null);
        if (restaurant == null) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "Restaurant not found", null), HttpStatus.NOT_FOUND);
        } else {
            List<TakeAway> orders = takeAwayRepository.findByRestaurantId(restaurant.getRestaurantId());
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Success", orders), HttpStatus.OK);
        }
    }

    @PostMapping("/dinedynamo/restaurant/takeaway/update")
    public ResponseEntity<ApiResponse> updateTakeAwayOrder(@RequestBody TakeAway takeAway) {
        Optional<TakeAway> existingOrder = takeAwayRepository.findById(takeAway.getTakeAwayId());
        if (existingOrder.isPresent()) {
            takeAwayRepository.save(takeAway);
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Success", null), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "TakeAway order not found", null), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/dinedynamo/restaurant/takeaway/delete")
    public ResponseEntity<ApiResponse> deleteTakeAwayOrder(@RequestBody TakeAway takeAway) {
        Optional<TakeAway> deleteOrder = takeAwayRepository.findById(takeAway.getTakeAwayId());
        if (deleteOrder.isPresent()) {
            takeAwayRepository.deleteById(takeAway.getTakeAwayId());
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Success", null), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "TakeAway order not found", null), HttpStatus.NOT_FOUND);
        }
    }
}
