package com.dinedynamo.controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.Menu;
import com.dinedynamo.collections.Restaurant;
import com.dinedynamo.repositories.MenuRepository;
import com.dinedynamo.repositories.RestaurantRepository;
import com.dinedynamo.services.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class MenuController
{

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    MenuService menuService;


    //Pass restaurantId in RequestBody to get the menu
    @PostMapping("/dinedynamo/restaurant/menu/getmenu")
    public ResponseEntity<ApiResponse> getMenu(@RequestBody Restaurant restaurant){

        restaurant = restaurantRepository.findById(restaurant.getRestaurantId()).get();

        Menu menu = menuService.findByRestaurantId(restaurant.getRestaurantId());

        return new ResponseEntity<ApiResponse>(new ApiResponse(HttpStatus.OK,"success",menu),HttpStatus.OK);

    }

    @DeleteMapping("/dinedynamo/restaurant/menu/deletewholemenu")
    public ResponseEntity<ApiResponse> deleteWholeMenu(@RequestBody Restaurant restaurant){

        restaurant = restaurantRepository.findById(restaurant.getRestaurantId()).get();

        Menu menu = menuService.findByRestaurantId(restaurant.getRestaurantId());

        menuRepository.delete(menu);

        return new ResponseEntity<ApiResponse>(new ApiResponse(HttpStatus.OK,"success",menu),HttpStatus.OK);

    }


    /**
     *
     * @param newMenu
     * @return The menu that is added to the database - (newMenu)
     * This method is used to add new menu or update the existing menu
     */
    @PutMapping("/dinedynamo/restaurant/menu/editmenu")
    public ResponseEntity<ApiResponse> editMenu(@RequestBody Menu newMenu){

        Restaurant restaurant = restaurantRepository.findById(newMenu.getRestaurantId()).get();

        Menu existingMenu = menuService.findByRestaurantId(restaurant.getRestaurantId());

        if(existingMenu != null){
            newMenu.setMenuId(existingMenu.getMenuId());
            newMenu.setRestaurantId(existingMenu.getRestaurantId());

            menuRepository.delete(existingMenu);
            menuRepository.save(newMenu);

        }
        else{
            menuRepository.save(newMenu);
        }
        return new ResponseEntity<ApiResponse>(new ApiResponse(HttpStatus.OK,"success",newMenu),HttpStatus.OK);
    }



    @DeleteMapping("/dinedynamo/restaurant/menu/deletepartailmenu")
    public ResponseEntity<ApiResponse> deletePartialMenu(@RequestBody Menu newMenu){

        Restaurant restaurant = restaurantRepository.findById(newMenu.getRestaurantId()).get();

        Menu existingMenu = menuService.findByRestaurantId(restaurant.getRestaurantId());

        newMenu.setMenuId(existingMenu.getMenuId());
        newMenu.setRestaurantId(existingMenu.getRestaurantId());

        menuRepository.delete(existingMenu);
        menuRepository.save(newMenu);


        return new ResponseEntity<ApiResponse>(new ApiResponse(HttpStatus.OK,"success",newMenu),HttpStatus.OK);

    }


}
