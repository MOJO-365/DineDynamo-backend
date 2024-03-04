package com.dinedynamo.controllers.restaurant_controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.restaurant_collections.AppUser;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.dto.restaurant_dtos.EditAppUserDTO;
import com.dinedynamo.repositories.restaurant_repositories.AppUserRepository;
import com.dinedynamo.services.restaurant_services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class AppUserController {

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    AppUserService appUserService;

    @PostMapping("/dinedynamo/restaurant/users/create-app-user")
    public ResponseEntity<ApiResponse> createAppUser(@RequestBody AppUser appUser){

        appUser = appUserService.save(appUser);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",appUser),HttpStatus.OK);

    }

    @DeleteMapping("/dinedynamo/restaurant/users/delete-app-user")
    public ResponseEntity<ApiResponse> deleteAppUser(@RequestBody AppUser appUser){

        appUserRepository.delete(appUser);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",appUser),HttpStatus.OK);

    }

    @PutMapping("/dinedynamo/restaurant/users/edit-app-user")
    public ResponseEntity<ApiResponse> editAppUser(@RequestBody EditAppUserDTO editAppUserDTO){

        AppUser appUser = appUserService.editAppuser(editAppUserDTO.getUserId(), editAppUserDTO.getAppUser());
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",appUser),HttpStatus.OK);

    }

    @PostMapping("/dinedynamo/restaurant/users/get-all-app-users-for-restaurant")
    public ResponseEntity<ApiResponse> getAllAppUsersForRestaurant(@RequestBody Restaurant restaurant){

        List<AppUser> appUserList = appUserService.findAllUsersByRestaurantId(restaurant);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",appUserList),HttpStatus.OK);

    }

    @DeleteMapping("/dinedynamo/restaurant/users/delete-all-app-users-for-restaurant")
    public ResponseEntity<ApiResponse> deleteAllAppUsersForRestaurant(@RequestBody Restaurant restaurant){

        List<AppUser> appUserList = appUserService.deleteAllUsersByRestaurantId(restaurant);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",appUserList),HttpStatus.OK);

    }
}
