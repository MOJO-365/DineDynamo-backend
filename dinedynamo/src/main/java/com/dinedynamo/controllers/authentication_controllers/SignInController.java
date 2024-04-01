package com.dinedynamo.controllers.authentication_controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.restaurant_collections.AppUser;
import com.dinedynamo.collections.restaurant_collections.RefreshToken;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.config.jwt_config.UserDetailsServiceImpl;
import com.dinedynamo.dto.authentication_dtos.SignInRequestBody;
import com.dinedynamo.helper.JwtHelper;
import com.dinedynamo.repositories.customer_repositories.CustomerRepository;
import com.dinedynamo.repositories.restaurant_repositories.AppUserRepository;
import com.dinedynamo.repositories.restaurant_repositories.RestaurantRepository;

import com.dinedynamo.services.restaurant_services.AppUserService;
import com.dinedynamo.services.restaurant_services.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.dinedynamo.dto.authentication_dtos.JwtResponseDTO;

@RestController
@CrossOrigin("*")
public class SignInController
{

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    JwtHelper jwtHelper;

    @Autowired
    AppUserService appUserService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    RefreshTokenService refreshTokenService;




    @PostMapping("/dinedynamo/auth/signin")
    public ResponseEntity<ApiResponse> signInWithJWT(@RequestBody SignInRequestBody signInRequestBody) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        System.out.println("in signin controller: "+signInRequestBody.getUserEmail());
        System.out.println("in signin controller: "+signInRequestBody.getUserPassword());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequestBody.getUserEmail(),
                        signInRequestBody.getUserPassword()
                )
        );

        if(authentication.isAuthenticated()){
            String accessToken = jwtHelper.generateToken(appUserRepository.findByUserEmail(signInRequestBody.getUserEmail()).orElse(null));
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(signInRequestBody.getUserEmail());
            JwtResponseDTO jwtResponseDTO = new JwtResponseDTO();
            jwtResponseDTO.setAccessToken(accessToken);
            jwtResponseDTO.setRefreshToken(refreshToken.getRefreshToken());
            jwtResponseDTO.setRestaurantId(jwtHelper.extractRestaurantId(accessToken));
            return new ResponseEntity<ApiResponse>(new ApiResponse(HttpStatus.OK,"success",jwtResponseDTO),HttpStatus.OK);
        }
        else{

            throw new UsernameNotFoundException("Invalid User request");
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