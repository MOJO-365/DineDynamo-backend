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
    //Search by restaurant name or menu
    ResponseEntity<ApiResponse> filterByExpression(@RequestParam String expression){

        List<Restaurant>  listOfRestaurantsFilteredByRestaurantName = restaurantRepository.findByRestaurantNameRegexIgnoreCase(expression);

        List<Restaurant> listOfRestaurantsFilteredByMenu = searchService.searchInMenusOfRestaurantsAndReturnRestaurantList(expression);

        Set<Restaurant> set = new HashSet<>();

        set.addAll(listOfRestaurantsFilteredByRestaurantName);

        set.addAll(listOfRestaurantsFilteredByMenu);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",new ArrayList<>(set)),HttpStatus.OK);

    }

    @PostMapping("/dinedynamo/customer/filters/sort-by-cost-asc")
    public ResponseEntity<ApiResponse> filterByCityAndCostForTwoAscending(@RequestParam String restaurantCity){

        List<Restaurant> restaurantList = restaurantRepository.findByRestaurantCityOrderByCostForTwoAsc(restaurantCity);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",restaurantList),HttpStatus.OK);
    }

    @PostMapping("/dinedynamo/customer/filters/sort-by-cost-desc")
    public ResponseEntity<ApiResponse> filterByCityAndCostForTwoDescending(@RequestParam String restaurantCity){

        List<Restaurant> restaurantList = restaurantRepository.findByRestaurantCityOrderByCostForTwoDesc(restaurantCity);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",restaurantList),HttpStatus.OK);
    }

    @PostMapping("/dinedynamo/customer/filters/filter-by-rating-gt")
    public ResponseEntity<ApiResponse> findRestaurantsWithGreaterThanRating(@RequestParam String restaurantCity, @RequestParam double rating){

        List<Restaurant> restaurantList = restaurantRepository.findByRestaurantCityAndRestaurantRatingGreaterThan(restaurantCity, rating);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",restaurantList),HttpStatus.OK);

    }


    @PostMapping("/dinedynamo/customer/filters/filter-by-cost-gt")
    public ResponseEntity<ApiResponse> findRestaurantsWithCostGreaterThan(@RequestParam String restaurantCity, @RequestParam double cost){

        List<Restaurant> restaurantList = restaurantRepository.findByRestaurantCityAndCostForTwoGreaterThan(restaurantCity, cost);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",restaurantList),HttpStatus.OK);

    }

    @PostMapping("/dinedynamo/customer/filters/filter-by-cost-between")
    public ResponseEntity<ApiResponse> findRestaurantsWithCostBetween(@RequestParam String restaurantCity, @RequestParam double maxCost, double minCost){

        List<Restaurant> restaurantList = restaurantRepository.findByRestaurantCityAndCostForTwoBetween(restaurantCity, minCost, maxCost);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",restaurantList),HttpStatus.OK);

    }

    ///////////////// For pure veg
    @PostMapping("/dinedynamo/customer/filters/filter-by-city-pure-veg")
    ResponseEntity<ApiResponse> filterByCityAndPureVeg(@RequestParam String restaurantCity){

        List<Restaurant> restaurantList = restaurantRepository.findAllPureVegRestaurantsByCity(restaurantCity);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",restaurantList),HttpStatus.OK);
    }


//    // Combination 1: Pure Veg, Ascending Cost
//    List<Restaurant> findByRestaurantCityAndIsPureVegOrderByCostForTwoAsc(String restaurantCity);

    @PostMapping("/dinedynamo/customer/filters/pure-veg/sort-by-cost-asc")
    public ResponseEntity<ApiResponse> filterByCityAndPureVegAndCostForTwoAscending(@RequestParam String restaurantCity){

        List<Restaurant> restaurantList = restaurantRepository.findByRestaurantCityAndIsPureVegOrderByCostForTwoAsc(restaurantCity);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",restaurantList),HttpStatus.OK);
    }


//    // Combination 2: Pure Veg, Descending Cost
//    List<Restaurant> findByRestaurantCityAndIsPureVegOrderByCostForTwoDesc(String restaurantCity);
//
    @PostMapping("/dinedynamo/customer/filters/pure-veg/sort-by-cost-desc")
    public ResponseEntity<ApiResponse> filterByCityAndPureVegAndCostForTwoDescending(@RequestParam String restaurantCity){

        List<Restaurant> restaurantList = restaurantRepository.findByRestaurantCityAndIsPureVegOrderByCostForTwoDesc(restaurantCity);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",restaurantList),HttpStatus.OK);
    }

//    // Combination 3: Pure Veg, Cost Between
//    List<Restaurant> findByRestaurantCityAndIsPureVegAndCostForTwoBetween(
//            String restaurantCity, boolean isPureVeg, double minCost, double maxCost
//    );

    @PostMapping("/dinedynamo/customer/filters/pure-veg/filter-by-cost-between")
    public ResponseEntity<ApiResponse> findRestaurantsWithCostBetweenAndPureVeg(@RequestParam String restaurantCity, @RequestParam double maxCost, @RequestParam  double minCost){

        List<Restaurant> restaurantList = restaurantRepository.findByRestaurantCityAndIsPureVegAndCostForTwoBetween(restaurantCity,true ,minCost, maxCost);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",restaurantList),HttpStatus.OK);

    }

//
//    // Combination 4: Pure Veg, Cost Greater Than
//    List<Restaurant> findByRestaurantCityAndIsPureVegAndCostForTwoGreaterThan(
//            String restaurantCity, boolean isPureVeg, double cost
//    );
    @PostMapping("/dinedynamo/customer/filters/pure-veg/filter-by-cost-gt")
    public ResponseEntity<ApiResponse> findRestaurantsWithCostGreaterThanAndPureVeg(@RequestParam String restaurantCity, @RequestParam double cost){

        List<Restaurant> restaurantList = restaurantRepository.findByRestaurantCityAndIsPureVegAndCostForTwoGreaterThan(restaurantCity,true ,cost);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",restaurantList),HttpStatus.OK);

    }
//
//    // Combination 5: Pure Veg, Rating Greater Than
//    List<Restaurant> findByRestaurantCityAndRestaurantRatingGreaterThanAndIsPureVeg(
//            String restaurantCity, double rating, boolean isPureVeg
//    );

    @PostMapping("/dinedynamo/customer/filters/pure-veg/filter-by-rating-gt")
    public ResponseEntity<ApiResponse> findRestaurantsWithGreaterThanRatingAndPureVeg(@RequestParam String restaurantCity, @RequestParam double rating){

        List<Restaurant> restaurantList = restaurantRepository.findByRestaurantCityAndRestaurantRatingGreaterThanAndIsPureVeg(restaurantCity, rating, true);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",restaurantList),HttpStatus.OK);

    }
}
