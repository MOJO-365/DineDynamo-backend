package com.dinedynamo.controllers;

import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.Restaurant;
import com.dinedynamo.repositories.RestaurantRepository;
import com.dinedynamo.services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class RestaurantController
{
    @Autowired
    RestaurantRepository restaurantRepository;


    @Autowired
    RestaurantService restaurantService;

    /**
     *
     * @param restaurant
     * In params, pass only the restaurant.restaurantCity
     * @return list of restaurants filtered by the city (If the city is empty, all restaurants will be returned)
     */
    @PostMapping("/dinedynamo/customer/findrestaurantsbycity")
    public ResponseEntity<ApiResponse> findRestaurantsByCity(@RequestBody Restaurant restaurant){

        String restaurantCity = restaurant.getRestaurantCity();
        if(restaurantCity.equals("") || restaurantCity.equals(" ") || restaurantCity == null){

            List<Restaurant> listOfAllRestaurants = restaurantRepository.findAll();
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",listOfAllRestaurants),HttpStatus.OK);
        }


        List<Restaurant> listFilteredByCity = restaurantRepository.findByRestaurantCity(restaurant.getRestaurantCity());

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",listFilteredByCity),HttpStatus.OK);


    }


    @PostMapping("/dinedynamo/restaurant/findrestaurantbyid")
    public ResponseEntity<ApiResponse> findRestaurantById(@RequestBody Restaurant restaurant){

        restaurant = restaurantRepository.findById(restaurant.getRestaurantId()).orElse(null);

        if(restaurant == null){
            System.out.println("RESTAURANT-ID DOES NOT EXIST");
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",null),HttpStatus.OK);

        }

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",restaurant),HttpStatus.OK);

    }


    @PutMapping("/dinedynamo/restaurant/editrestaurant")
    public ResponseEntity<ApiResponse> editRestaurant(@RequestBody Restaurant newRestaurant){

        restaurantRepository.save(newRestaurant);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",newRestaurant),HttpStatus.OK);

    }


    @GetMapping("/dinedynamo/restaurant/getall")
    public ResponseEntity<Page<Restaurant>> getAllRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        Page<Restaurant> restaurants = restaurantService.getAllRestaurants(page, size);
        return ResponseEntity.ok(restaurants);
    }


}
