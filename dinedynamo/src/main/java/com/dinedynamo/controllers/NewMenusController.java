package com.dinedynamo.controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.Restaurant;
import com.dinedynamo.collections.menu_collections.Menus;
import com.dinedynamo.repositories.menu_repositories.MenusRepository;
import com.dinedynamo.services.MenusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class NewMenusController
{

    @Autowired
    private MenusRepository menusRepository;

    @Autowired
    MenusService menusService;

    @PostMapping("/dinedynamo/restaurant/menu/create-menu")
    public ResponseEntity<ApiResponse> createMenu(@RequestBody Menus menus) {
        menusService.save(menus);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",menus),HttpStatus.OK);
    }


    @DeleteMapping("/dinedynamo/restaurant/menu/delete-menu")
    public ResponseEntity<ApiResponse> deletemenu(@RequestBody Restaurant restaurant){
        boolean isDeleted = menusService.deleteMenus(restaurant.getRestaurantId());
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",isDeleted),HttpStatus.OK);
    }


}
