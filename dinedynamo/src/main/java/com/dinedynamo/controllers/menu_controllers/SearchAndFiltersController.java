package com.dinedynamo.controllers.menu_controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.repositories.MenuRepository;
import com.dinedynamo.repositories.restaurant_repositories.RestaurantRepository;
import com.dinedynamo.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin("*")
public class SearchAndFiltersController
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


    /**
     *
     * @param expression
     * @return List of Restaurant objects that match the cuisine or item name (or expression)
     * This api searched the expression in the Menu object and returns the corresponding restaurants
     */
    @PostMapping("/dinedynamo/customer/filters/filter-by-cuisine-or-item")
    ResponseEntity<ApiResponse> filterByMenuCuisineOrItem(@RequestParam String expression){

        List<Restaurant> listOfRestaurants = searchService.searchInMenusOfRestaurantsAndReturnRestaurantList(expression);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",listOfRestaurants),HttpStatus.OK);

    }


    /**
     *
     * @param expression
     * @return Restaurant List
     * Use: In customer dashboard, there is a search field. That field can have - search by restaurant name or search by menu item
     * This api searched the database by the expression (irrespective of restaurant
     * name or menu item or cuisine) and returns the list of restaurants that match the expression
     */
    @PostMapping("/dinedynamo/customer/filters/filter-by-expression")
    ResponseEntity<ApiResponse> filterByExpression(@RequestParam String expression){

        List<Restaurant>  listOfRestaurantsFilteredByRestaurantName = restaurantRepository.findByRestaurantNameRegexIgnoreCase(expression);

        List<Restaurant> listOfRestaurantsFilteredByMenu = searchService.searchInMenusOfRestaurantsAndReturnRestaurantList(expression);

        Set<Restaurant> set = new HashSet<>();

        set.addAll(listOfRestaurantsFilteredByRestaurantName);

        set.addAll(listOfRestaurantsFilteredByMenu);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",new ArrayList<>(set)),HttpStatus.OK);

    }

    @PostMapping("/dinedynamo/customer/filters/filter-by-city-pure-veg")
    ResponseEntity<ApiResponse> filterByCityAndPureVeg(@RequestParam String restaurantCity){

        List<Restaurant> restaurantList = restaurantRepository.findAllPureVegRestaurantsByCity(restaurantCity);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",restaurantList),HttpStatus.OK);
    }


    @PostMapping("/dinedynamo/customer/filters/filter-by-city-non-veg-and-veg")
    ResponseEntity<ApiResponse> filterByCityAndVegAndNonVeg(@RequestParam String restaurantCity){

        List<Restaurant> restaurantList = restaurantRepository.findAllNonPureVegRestaurantsByCity(restaurantCity);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",restaurantList),HttpStatus.OK);
    }


}
