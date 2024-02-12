package com.dinedynamo.controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.Restaurant;
import com.dinedynamo.repositories.MenuRepository;
import com.dinedynamo.repositories.RestaurantRepository;
import com.dinedynamo.services.SearchService;
import org.apache.coyote.Response;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.dinedynamo.collections.Menu;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin("*")
public class SearchController
{

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    SearchService searchService;



    @PostMapping("/dinedynamo/customer/filters/filter-by-restaurantname")
    ResponseEntity<ApiResponse> filterByRestaurantName(@RequestParam String restaurantName){

        List<Restaurant>  listOfRestaurantsFilteredByRestaurantName   = restaurantRepository.findByRestaurantNameRegexIgnoreCase(restaurantName);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",listOfRestaurantsFilteredByRestaurantName),HttpStatus.OK);

    }


    @PostMapping("/dinedynamo/customer/filters/filter-by-cuisine-or-item")
    ResponseEntity<ApiResponse> filterByMenuCuisineOrItem(@RequestParam String expression){

        List<Restaurant> listOfRestaurants = searchService.searchInMenuOfRestaurantsAndReturnRestaurantList(expression);






        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",listOfRestaurants),HttpStatus.OK);

    }


    @PostMapping("/dinedynamo/customer/filters/filter-by-expression")
    ResponseEntity<ApiResponse> filterByExpression(@RequestParam String expression){

        List<Restaurant>  listOfRestaurantsFilteredByRestaurantName   = restaurantRepository.findByRestaurantNameRegexIgnoreCase(expression);



        List<Restaurant> listOfRestaurantsFilteredByMenu = searchService.searchInMenuOfRestaurantsAndReturnRestaurantList(expression);


        Set<Restaurant> set = new HashSet<>();

        // Add elements from list1
        set.addAll(listOfRestaurantsFilteredByRestaurantName);

        // Add elements from list2
        set.addAll(listOfRestaurantsFilteredByMenu);





        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",new ArrayList<>(set)),HttpStatus.OK);



    }



}
