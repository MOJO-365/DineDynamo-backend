package com.dinedynamo.controllers.restaurant_controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.subscriptions_collections.RestaurantSubscription;
import com.dinedynamo.dto.subscription_dtos.SubscriptionRequestDTO;
import com.dinedynamo.repositories.subscription_repositories.RestaurantSubscriptionRepository;
import com.dinedynamo.services.subscription_services.RestaurantSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
public class RestaurantSubscriptionController
{

    @Autowired
    RestaurantSubscriptionRepository restaurantSubscriptionRepository;



    @Autowired
    RestaurantSubscriptionService restaurantSubscriptionService;

    @PostMapping("/dinedynamo/restaurant/subscriptions/take-subscription")
    public ResponseEntity<ApiResponse> takeSubscription(@RequestBody SubscriptionRequestDTO subscriptionRequestDTO){

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",restaurantSubscriptionService.save(subscriptionRequestDTO)),HttpStatus.OK);
    }


    @PostMapping("/dinedynamo/restaurant/subscriptions/get-subscription-status")
    public ResponseEntity<ApiResponse> getSubscriptionStatusForRestaurant(@RequestParam String restaurantId){

        RestaurantSubscription restaurantSubscription = restaurantSubscriptionRepository.findByRestaurantId(restaurantId);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",restaurantSubscription),HttpStatus.OK);


    }
}
