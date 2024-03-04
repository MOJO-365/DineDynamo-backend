package com.dinedynamo.controllers.new_orders_controllers;

import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.collections.order_collections.TakeAway;
import com.dinedynamo.repositories.order_repositories.NewOrderRepository;
import com.dinedynamo.repositories.restaurant_repositories.RestaurantRepository;
import com.dinedynamo.repositories.order_repositories.NewTakeAwayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
public class NewTakeAwayController {

    @Autowired
    private NewTakeAwayRepository newTakeAwayRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @PostMapping("/dinedynamo/restaurant/takeaway/place")
    public ResponseEntity<ApiResponse> createTakeAwayOrder(@RequestBody TakeAway takeAway) {
        takeAway.setDateTime(LocalDateTime.now());
        TakeAway savedOrder = newTakeAwayRepository.save(takeAway);
        ApiResponse response = new ApiResponse(HttpStatus.OK, "Success", savedOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/dinedynamo/restaurant/takeaway/all")
    public ResponseEntity<ApiResponse> getAllTakeAwayOrders(@RequestBody Restaurant restaurant) {
        restaurant = restaurantRepository.findById(restaurant.getRestaurantId()).orElse(null);
        if (restaurant == null) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "Restaurant not found", null), HttpStatus.NOT_FOUND);
        } else {
            List<TakeAway> orders = newTakeAwayRepository.findByRestaurantId(restaurant.getRestaurantId());
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Success", orders), HttpStatus.OK);
        }
    }

    @PostMapping("/dinedynamo/restaurant/takeaway/update")
    public ResponseEntity<ApiResponse> updateTakeAwayOrder(@RequestBody TakeAway updatedTakeAway) {
        Optional<TakeAway> existingTakeAwayOptional = newTakeAwayRepository.findById(updatedTakeAway.getTakeAwayId());

        if (existingTakeAwayOptional.isPresent()) {
            TakeAway existingTakeAway = existingTakeAwayOptional.get();
            existingTakeAway.setPickedUp(updatedTakeAway.isPickedUp());
            existingTakeAway.setOrderList(updatedTakeAway.getOrderList());

            newTakeAwayRepository.save(existingTakeAway);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "TakeAway order updated successfully", null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(HttpStatus.NOT_FOUND, "TakeAway order not found", null));
        }
    }


    @DeleteMapping("/dinedynamo/restaurant/takeaway/delete")
    public ResponseEntity<ApiResponse> deleteTakeAwayOrder(@RequestBody TakeAway takeAway) {
        Optional<TakeAway> deleteOrder = newTakeAwayRepository.findById(takeAway.getTakeAwayId());
        if (deleteOrder.isPresent()) {
            newTakeAwayRepository.deleteById(takeAway.getTakeAwayId());
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Success", null), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "TakeAway order not found", null), HttpStatus.NOT_FOUND);
        }
    }
}
