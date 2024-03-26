package com.dinedynamo.controllers.authentication_controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.restaurant_collections.AppUser;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.config.jwt_config.UserDetailsServiceImpl;
import com.dinedynamo.dto.authentication_dtos.SignInRequestBody;
import com.dinedynamo.helper.JwtHelper;
import com.dinedynamo.repositories.customer_repositories.CustomerRepository;
import com.dinedynamo.repositories.restaurant_repositories.AppUserRepository;
import com.dinedynamo.repositories.restaurant_repositories.RestaurantRepository;

import com.dinedynamo.services.restaurant_services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@CrossOrigin("*")
public class SignInController
{

    @Autowired
    RestaurantRepository restaurantRepository;


    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    JwtHelper jwtHelper;

    @Autowired
    AppUserService appUserService;

    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private AuthenticationManager authenticationManager;

    UserDetails userDetails;


//    @PostMapping("/generateToken")
//    public String authenticateAndGetToken(@RequestBody JwtAuthRequest jwtAuthRequest) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
//
//        boolean isUserAuthenticated = authenticate(jwtAuthRequest.getUserEmail(), jwtAuthRequest.getUserPassword());
//
//        System.out.println("Before if block.....");
//        if (isUserAuthenticated) {
//            System.out.println("AUTHENTICATED..");
//            userDetails = this.userDetailsServiceImpl.loadUserByUsername(jwtAuthRequest.getUserEmail());
//            System.out.println(userDetails.getUsername());
//            return jwtHelper.generateToken(userDetails,jwtAuthRequest);
//        }
//
//        else {
//            System.out.println("reached else block");
//            throw new UsernameNotFoundException("invalid user request !");
//        }
//    }


    @PostMapping("/dinedynamo/auth/signin")
    public ResponseEntity<ApiResponse> signInWithJWT(@RequestBody SignInRequestBody signInRequestBody) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String token = null;
        String userRole = null;
        AppUser appUser = appUserRepository.findByUserEmail(signInRequestBody.getUserEmail()).orElse(null);
        boolean isUserAuthenticated = authenticateAppUserSignIn(signInRequestBody.getUserEmail(), signInRequestBody.getUserPassword());

        if(appUser == null){
            throw new RuntimeException("App user not found in DB");
        }

        if(!isUserAuthenticated){
            System.out.println("WRONG USERNAME OR PASSWORD");
            return new ResponseEntity<ApiResponse>(new ApiResponse(HttpStatus.NOT_FOUND,"success",null),HttpStatus.OK);
        }
        else{
            userRole = appUser.getUserType();
            this.userDetailsServiceImpl.setUserRole(userRole);
            System.out.println("In SignIn controller: USER AUTHENTICATED, NOW TOKEN WILL BE GENERATED");
            userDetails = this.userDetailsServiceImpl.loadUserByUsername(signInRequestBody.getUserEmail());
            System.out.println("USER-EMAIL: "+userDetails.getUsername());
            token = jwtHelper.generateToken(userDetails,signInRequestBody);
            return new ResponseEntity<ApiResponse>(new ApiResponse(HttpStatus.OK,"success",token),HttpStatus.OK);
        }

    }

    @PostMapping("/dinedynamo/signin")
    public ResponseEntity<ApiResponse> signIn(@RequestBody SignInRequestBody signInRequestBody) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        AppUser appUser = appUserRepository.findByUserEmail(signInRequestBody.getUserEmail()).orElse(null);


        boolean isUserAuthenticated = authenticateAppUserSignIn(signInRequestBody.getUserEmail(), signInRequestBody.getUserPassword());

        if(!isUserAuthenticated){
            System.out.println("WRONG USERNAME OR PASSWORD");
            return new ResponseEntity<ApiResponse>(new ApiResponse(HttpStatus.NOT_FOUND,"success",null),HttpStatus.OK);
        }
        else{
            appUser = appUserRepository.findByUserEmail(signInRequestBody.getUserEmail()).orElse(null);
            return new ResponseEntity<ApiResponse>(new ApiResponse(HttpStatus.NOT_FOUND,"success",appUser),HttpStatus.OK);


        }


/////////////////////////////////////
//        String token = null;
//
//        if(signInRequestBody.getUserType().toUpperCase().equals("RESTAURANT")){
//
//            boolean isAuthenticated = authenticateRestaurantSignIn(signInRequestBody.getUserEmail(), signInRequestBody.getUserPassword());
//
//            if(!isAuthenticated){
//                System.out.println("USER-EMAIL AND PASSWORD OF RESTAURANT ARE INCORRECT - AUTHENTICATION FAILED");
//                throw new UsernameNotFoundException("invalid user request !");
//            }
//
//            this.userDetailsServiceImpl.setUserRole(userRole);
//            System.out.println("In SignIn controller: USER AUTHENTICATED, NOW TOKEN WILL BE GENERATED");
//            userDetails = this.userDetailsServiceImpl.loadUserByUsername(signInRequestBody.getUserEmail());
//            System.out.println("USER-EMAIL: "+userDetails.getUsername());
//            token = jwtHelper.generateToken(userDetails,signInRequestBody);
//
//        }
//        else if(signInRequestBody.getUserType().toUpperCase().equals("CUSTOMER")){
//
//
//            boolean isAutheticated = authenticateCustomerSignIn(signInRequestBody.getUserEmail(), signInRequestBody.getUserPassword());
//
//            if(!isAutheticated){
//                System.out.println("USER-EMAIL AND PASSWORD OF CUSTOMER ARE INCORRECT - AUTHENTICATION FAILED");
//                throw new UsernameNotFoundException("invalid user request !");
//            }
//
//            this.userDetailsServiceImpl.setUserRole(userRole);
//            System.out.println("In SignIn controller: USER AUTHENTICATED, NOW TOKEN WILL BE GENERATED");
//            userDetails = this.userDetailsServiceImpl.loadUserByUsername(signInRequestBody.getUserEmail());
//            System.out.println("USER-EMAIL: "+userDetails.getUsername());
//            token = jwtHelper.generateToken(userDetails,signInRequestBody);
//
//        }
//
//        System.out.println("GENERATED TOKEN: "+token);
//        return new ResponseEntity<ApiResponse>(new ApiResponse(HttpStatus.OK,"success",token),HttpStatus.OK);

    }

    private boolean authenticateRestaurantSignIn(String userEmail, String userPassword){

        return true;
//        Restaurant restaurant = restaurantRepository.findByRestaurantEmail(userEmail).orElse(null);
//        if (restaurant == null){
//            System.out.println("RESTAURANT DOES NOT EXIST IN THE DB, AUTHENTICATION FAILED");
//            return false;
//        }
//        String userPasswordFromDB = restaurant.getRestaurantPassword();
//
//
//        if(userPasswordFromDB.equals(userPassword)){
//            return true;
//        }
//        return false;
    }





    private boolean authenticateAppUserSignIn(String userEmail, String userPassword){

        AppUser appUserFromDB = appUserRepository.findByUserEmail(userEmail).orElse(null);
        if(appUserFromDB == null){

            Restaurant restaurant = restaurantRepository.findByRestaurantEmail(userEmail).orElse(null);

            if(restaurant==null){
                throw new RuntimeException("No such user/restaurant found in database");

            }
            else{
                AppUser appUser = appUserService.saveRestaurant(restaurant);

                appUserFromDB = appUserRepository.findByUserEmail(userEmail).orElse(null);

                if(appUser.getUserPassword().equals(userPassword)){
                    return true;
                }
                else{
                    return false;
                }
            }


        }
        else {
            return appUserFromDB.getUserPassword().equals(userPassword);
        }
    }
}