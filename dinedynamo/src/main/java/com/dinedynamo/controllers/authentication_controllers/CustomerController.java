package com.dinedynamo.controllers.authentication_controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.CustomerFavouriteRestaurants;
import com.dinedynamo.collections.authentication_collections.Restaurant;
import com.dinedynamo.repositories.RestaurantRepository;
import com.dinedynamo.repositories.order_repositories.CustomerFavouriteRestaurantsRespository;
import com.dinedynamo.services.CustomerFavouriteRestaurantsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin("*")
public class CustomerController {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    CustomerFavouriteRestaurantsService customerFavouriteRestaurantsService;

    @Autowired
    CustomerFavouriteRestaurantsRespository customerFavouriteRestaurantsRespository;

    @PostMapping("/dinedynamo/customer/favourites/add-to-favs")
    ResponseEntity<ApiResponse> addToFavourites(@RequestParam String customerPhone, @RequestParam String restaurantId){

        System.out.println("PHONE: "+customerPhone);
        System.out.println("RESTAURANT-ID: "+restaurantId);
        CustomerFavouriteRestaurants customerFavouriteRestaurants = customerFavouriteRestaurantsService.addToFavourites(customerPhone,restaurantId);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",customerFavouriteRestaurants),HttpStatus.OK);
    }

    @PostMapping("/dinedynamo/customer/favourites/get-all-favs")
    ResponseEntity<ApiResponse> getAllFavourites(@RequestParam String customerPhone){

        System.out.println("CUSTOMER PHONE: "+customerPhone);
        List<Restaurant> restaurantList = customerFavouriteRestaurantsService.getAllFavourites(customerPhone);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",restaurantList),HttpStatus.OK);
    }


    @PostMapping("/dinedynamo/customer/favourites/delete-from-favs")
    ResponseEntity<ApiResponse> deleteFromFavourites(@RequestParam String customerPhone, @RequestParam String restaurantId){

        System.out.println("CUSTOMER PHONE: "+customerPhone);
        System.out.println("RESTAURANT-ID: "+restaurantId);
        List<String> listOfFavs = customerFavouriteRestaurantsService.deleteFromFavourites(customerPhone,restaurantId);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",listOfFavs),HttpStatus.OK);


    }


}
