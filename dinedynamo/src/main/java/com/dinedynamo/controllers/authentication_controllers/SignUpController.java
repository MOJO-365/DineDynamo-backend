package com.dinedynamo.controllers.authentication_controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.customer_collections.Customer;
import com.dinedynamo.collections.restaurant_collections.AppUser;
import com.dinedynamo.collections.restaurant_collections.RefreshToken;
import com.dinedynamo.collections.restaurant_collections.Restaurant;

import com.dinedynamo.dto.authentication_dtos.JwtResponseDTO;
import com.dinedynamo.dto.authentication_dtos.SignInRequestBody;
import com.dinedynamo.helper.JwtHelper;
import com.dinedynamo.repositories.customer_repositories.CustomerRepository;
import com.dinedynamo.repositories.restaurant_repositories.AppUserRepository;
import com.dinedynamo.repositories.restaurant_repositories.RefreshTokenRepository;
import com.dinedynamo.repositories.restaurant_repositories.RestaurantRepository;

import com.dinedynamo.services.restaurant_services.AppUserService;
import com.dinedynamo.services.restaurant_services.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController

@CrossOrigin("*")
public class SignUpController
{

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AppUserService appUserService;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    JwtHelper jwtHelper;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/dinedynamo/home")
    public String testRestaurantController(){
        return "Restaurant controller working";
    }



    @PostMapping("/dinedynamo/signuprestaurant")
    public ResponseEntity<ApiResponse> restaurantSignUp(@RequestBody Restaurant restaurant) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        System.out.println(restaurantRepository.findByRestaurantEmail(restaurant.getRestaurantEmail()));

        Restaurant existingRestaurant = restaurantRepository.findByRestaurantEmail(restaurant.getRestaurantEmail()).orElse(null);

        if(existingRestaurant != null){
            System.out.println("Restaurant email already exists in database");
            return new ResponseEntity<ApiResponse>(new ApiResponse(HttpStatus.CONFLICT,"success",null),HttpStatus.OK);
        }

        else{

            String password = restaurant.getRestaurantPassword();
            restaurant.setRestaurantPassword(passwordEncoder.encode(password));
            restaurantRepository.save(restaurant);
            appUserService.saveRestaurant(restaurant);

            System.out.println("Data of restaurant saved");

//            String accessToken = jwtHelper.generateToken(appUserRepository.findByUserEmail(restaurant.getRestaurantEmail()).orElse(null));
//            System.out.println("TOKEN AFTER SIGNUP: "+accessToken);
//
//            RefreshToken refreshToken = refreshTokenService.createRefreshToken(restaurant.getRestaurantEmail());
//            JwtResponseDTO jwtResponseDTO = new JwtResponseDTO();
//            jwtResponseDTO.setAccessToken(accessToken);
//            jwtResponseDTO.setRefreshToken(refreshToken.getRefreshToken());
            return new ResponseEntity<ApiResponse>(new ApiResponse(HttpStatus.OK,"success",true),HttpStatus.OK);

        }


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