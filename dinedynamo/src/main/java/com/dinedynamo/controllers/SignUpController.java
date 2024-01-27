package com.dinedynamo.controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.Customer;
import com.dinedynamo.collections.Restaurant;

import com.dinedynamo.repositories.CustomerRepository;
import com.dinedynamo.repositories.RestaurantRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

@CrossOrigin("*")
public class SignUpController
{

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    CustomerRepository customerRepository;


    @GetMapping("/dinedynamo/home")
    public String testRestaurantController(){
        return "Restaurant controller working";
    }



    @PostMapping("/dinedynamo/signuprestaurant")
    public ResponseEntity<ApiResponse> restaurantSignUp(@RequestBody Restaurant restaurant)
    {

        System.out.println(restaurantRepository.findByRestaurantEmail(restaurant.getRestaurantEmail()));

        //System.out.println();
        if(restaurantRepository.findByRestaurantEmail(restaurant.getRestaurantEmail()).orElse(null) == null)
        {

            restaurantRepository.save(restaurant);
            System.out.println("Data of restaurant saved");
            ApiResponse apiResponse = new ApiResponse(HttpStatus.OK,"success",restaurant);


            //Now as the account of restaurant is created, create the same in 'Users' collection and store it for the purpose of JWT
//            User restaurantUser = userService.createUser(restaurant);
//            userService.save(restaurantUser);


            return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
        }

        System.out.println("Data already exists");

        return new ResponseEntity<ApiResponse>(new ApiResponse(HttpStatus.CONFLICT,"success"),HttpStatus.OK);

    }


    @PostMapping("/dinedynamo/signupcustomer")
    public ResponseEntity<ApiResponse> customerSignUp(@RequestBody Customer customer)
    {
        System.out.println("In SignUpController, name: "+customer.getCustomerName());
        if(customerRepository.findByCustomerEmail(customer.getCustomerEmail()).orElse(null) == null)
        {

            customerRepository.save(customer);

            System.out.println("Data of customer saved");
            ApiResponse apiResponse = new ApiResponse(HttpStatus.OK,"success",customer);

            //Now as the account of customer is created, create the same in 'Users' collection and store it
//            User customerUser = userService.createUser(customer);
//            userService.save(customerUser);

            return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
        }


        System.out.println("Data already exists");
        return new ResponseEntity<ApiResponse>(new ApiResponse(HttpStatus.CONFLICT,"success"),HttpStatus.OK);

    }




}
