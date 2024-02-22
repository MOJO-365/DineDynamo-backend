package com.dinedynamo.controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.Restaurant;
import com.dinedynamo.collections.menu_collections.*;
import com.dinedynamo.dto.edit_menu_dtos.EditMenuItemDTO;
import com.dinedynamo.repositories.menu_repositories.MenusRepository;
import com.dinedynamo.services.MenusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/dinedynamo/restaurant/menu/get-menu")
    public ResponseEntity<ApiResponse> getMenuOfRestaurant(@RequestBody Restaurant restaurant){
        Menus menus = menusRepository.findByRestaurantId(restaurant.getRestaurantId()).orElse(null);

        if(menus == null){
            System.out.println("MENU OF THIS RESTAURANT DOES NOT EXIST");
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",menus),HttpStatus.OK);
    }


    @PostMapping("/dinedynamo/restaurant/menu/categories")
    public List<Category> getCategories(){

        Menus menus = menusRepository.findById("65d198e184210b4917ef7761").orElse(null);

        assert menus != null;
        List<Category> l = menus.getListOfCategories();

        return l;

    }


    @PostMapping("/dinedynamo/restaurant/menu/add-item-to-category")
    public ResponseEntity<ApiResponse> addItemToCategory(@RequestBody MenuItem menuItem){

        Menus menus = menusService.addMenuItemInCategory(menuItem);

        if(menus == null){

            System.out.println("INVALID REQUEST BODY, PASS RESTAURANT-ID, PARENT-ID");
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",menus),HttpStatus.OK);

    }

    @PostMapping("/dinedynamo/restaurant/menu/add-item-to-sub-category")
    public ResponseEntity<ApiResponse> addItemToSubCategory(@RequestBody MenuItem menuItem){

        Menus menus = menusService.addMenuItemInSubCategory(menuItem);

        if(menus == null){

            System.out.println("INVALID REQUEST BODY, PASS RESTAURANT-ID, PARENT-ID");
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",menus),HttpStatus.OK);

    }

    @PostMapping("/dinedynamo/restaurant/menu/add-item-to-sub-sub-category")
    public ResponseEntity<ApiResponse> addItemToSubSubCategory(@RequestBody MenuItem menuItem){

        Menus menus = menusService.addMenuItemInSubSubCategory(menuItem);

        if(menus == null){

            System.out.println("INVALID REQUEST BODY, PASS RESTAURANT-ID, PARENT-ID");
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",menus),HttpStatus.OK);

    }


    @PostMapping("/dinedynamo/restaurant/menu/add-category")
    public ResponseEntity<ApiResponse> addCategoryInMenu(@RequestBody Category category){

        Menus menus = menusService.addCategoryInMenu(category);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",menus),HttpStatus.OK);
    }


    @PostMapping("/dinedynamo/restaurant/menu/add-sub-category")
    public ResponseEntity<ApiResponse> addSubCategoryInMenu(@RequestBody SubCategory subCategory){

        Menus menus = menusService.addSubCategory(subCategory);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",menus),HttpStatus.OK);
    }


    @PostMapping("/dinedynamo/restaurant/menu/add-sub-sub-category")
    public ResponseEntity<ApiResponse> addSubSubCategoryInMenu(@RequestBody SubSubCategory subSubCategory){

        Menus menus = menusService.addSubSubCategory(subSubCategory);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",menus),HttpStatus.OK);
    }

    @DeleteMapping("/dinedynamo/restaurant/menu/delete-menu-item")
    public ResponseEntity<ApiResponse> deleteMenuItem(@RequestBody MenuItem menuItem){

        Menus menus = menusService.deleteMenuItem(menuItem);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",menus),HttpStatus.OK);
    }

    @DeleteMapping("/dinedynamo/restaurant/menu/delete-sub-sub-category")
    public ResponseEntity<ApiResponse> deleteMenuItem(@RequestBody SubSubCategory subSubCategory){

        Menus menus = menusService.deleteSubSubCategory(subSubCategory);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",menus),HttpStatus.OK);
    }

    @DeleteMapping("/dinedynamo/restaurant/menu/delete-sub-category")
    public ResponseEntity<ApiResponse> deleteMenuItem(@RequestBody SubCategory subCategory){

        Menus menus = menusService.deleteSubCategory(subCategory);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",menus),HttpStatus.OK);
    }


    @DeleteMapping("/dinedynamo/restaurant/menu/delete-category")
    public ResponseEntity<ApiResponse> deleteMenuItem(@RequestBody Category category){

        Menus menus = menusService.deleteCategory(category);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",menus),HttpStatus.OK);
    }

    @PutMapping("/dinedynamo/restaurant/menu/edit-menuitem")
    public ResponseEntity<ApiResponse> editMenuItem(@RequestBody EditMenuItemDTO editMenuItemDTO){

        Menus menus = menusService.editMenuItem(editMenuItemDTO.getItemId(), editMenuItemDTO.getMenuItem());

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",menus),HttpStatus.OK);
    }

}
