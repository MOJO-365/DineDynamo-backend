package com.dinedynamo.controllers.restaurant_controllers;

import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.dto.restaurant_dtos.EditRestaurantDTO;
import com.dinedynamo.repositories.restaurant_repositories.RestaurantRepository;
import com.dinedynamo.services.restaurant_services.AppUserService;
import com.dinedynamo.services.restaurant_services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin("*")
public class RestaurantController
{
    @Autowired
    RestaurantRepository restaurantRepository;


    @Autowired
    RestaurantService restaurantService;

    @Autowired
    AppUserService appUserService;

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

    /**
     *
     * @param restaurant
     * @return Restaurant object
     * Pass only restaurantId in request body to get the corresponding Restaurant object in response
     */
    @PostMapping("/dinedynamo/restaurant/findrestaurantbyid")
    public ResponseEntity<ApiResponse> findRestaurantById(@RequestBody Restaurant restaurant){

        restaurant = restaurantRepository.findById(restaurant.getRestaurantId()).orElse(null);

        if(restaurant == null){
            System.out.println("RESTAURANT-ID DOES NOT EXIST");
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",null),HttpStatus.OK);

        }

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",restaurant),HttpStatus.OK);

    }


    /**
     *
     * @param editRestaurantDTO
     * @return Restaurant
     * Use: When the restaurant profiles needs to be updated
     */
    @PutMapping("/dinedynamo/restaurant/editrestaurant")
    public ResponseEntity<ApiResponse> editRestaurant(@RequestBody EditRestaurantDTO editRestaurantDTO){


        System.out.println("RESTAURANT-ID: "+editRestaurantDTO.getRestaurantId());
        String restaurantIdFromRequest = editRestaurantDTO.getRestaurantId();

        Restaurant oldRestaurantDetails = restaurantRepository.findById(restaurantIdFromRequest).orElse(null);

        if(oldRestaurantDetails == null){
            throw new NoSuchElementException("This restaurant does not exist in database");
        }

        Restaurant newRestaurantDetails = editRestaurantDTO.getRestaurant();
        newRestaurantDetails.setRestaurantId(restaurantIdFromRequest);

        restaurantRepository.save(newRestaurantDetails);
        Restaurant updatedRestaurant = restaurantRepository.findByRestaurantId(editRestaurantDTO.getRestaurantId());
        appUserService.updateRestaurant(updatedRestaurant);


        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",newRestaurantDetails),HttpStatus.OK);

    }


    /**
     *
     * @param page : The page number that needs to be accessed
     * @param size : At a time how may restaurants need to be fetched in one page
     * @return: Returns the list of Restaurants in pagination format.
     */
    @GetMapping("/dinedynamo/restaurant/getall")
    public ResponseEntity<Page<Restaurant>> getAllRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {

        Page<Restaurant> restaurants = restaurantService.getAllRestaurants(page, size);
        restaurants.getTotalPages();

        System.out.println("TOTAL NO OF PAGES: "+restaurants.getTotalPages());

        return ResponseEntity.ok(restaurants);
    }



}
