package com.dinedynamo.controllers.inventory_controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.inventory_management.RawMaterial;
import com.dinedynamo.collections.inventory_management.ReplenishmentLog;
import com.dinedynamo.collections.inventory_management.WastageLog;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.repositories.inventory_repositories.RawMaterialRepository;
import com.dinedynamo.repositories.inventory_repositories.ReplenishmentLogRepository;
import com.dinedynamo.repositories.restaurant_repositories.RestaurantRepository;
import com.dinedynamo.services.inventory_services.ReplenishmentLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class ReplenishmentLogController {

    @Autowired
    RawMaterialRepository rawMaterialRepository;

    @Autowired
    ReplenishmentLogRepository replenishmentLogRepository;

    @Autowired
    ReplenishmentLogService replenishmentLogService;

    @Autowired
    RestaurantRepository restaurantRepository;

    @PostMapping("/dinedynamo/restaurant/inventory/add-replenishment-log")
    public ResponseEntity<ApiResponse> addReplenishmentLog(@RequestBody ReplenishmentLog replenishmentLog){

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",replenishmentLogService.save(replenishmentLog)),HttpStatus.OK);

    }

    @PostMapping("/dinedynamo/restaurant/inventory/get-all-replenishment-logs-for-raw-material")
    public ResponseEntity<ApiResponse> getReplenishmentLogsForRawMaterial(@RequestBody RawMaterial rawMaterial){

        rawMaterial = rawMaterialRepository.findById(rawMaterial.getRawMaterialId()).orElse(null);

        if(rawMaterial==null){
            System.out.println("RAW MATERIAL NOT FOUND IN DB, ID INCORRECT");
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND,"success",null),HttpStatus.OK);

        }
        else{
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",replenishmentLogRepository.findByRawMaterialId(rawMaterial.getRawMaterialId())),HttpStatus.OK);
        }
    }


    @PostMapping("/dinedynamo/restaurant/inventory/get-all-replenishment-logs-for-restaurant")
    public ResponseEntity<ApiResponse> getReplenishmentLogsForRestaurant(@RequestBody Restaurant restaurant){

        restaurant = restaurantRepository.findById(restaurant.getRestaurantId()).orElse(null);

        if(restaurant==null){
            System.out.println("RESTAURANT NOT FOUND IN DB, ID INCORRECT");
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND,"success",null),HttpStatus.OK);

        }
        else{
            List<ReplenishmentLog> replenishmentLogList = replenishmentLogRepository.findByRestaurantId(restaurant.getRestaurantId());
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",replenishmentLogList),HttpStatus.OK);
        }
    }

    // Is current level to be updated when the replenishment log is deleted?
    @DeleteMapping("/dinedynamo/restaurant/inventory/delete-replenishment-log")
    public ResponseEntity<ApiResponse> deleteReplenishmentLog(@RequestBody ReplenishmentLog replenishmentLog){

        replenishmentLogRepository.delete(replenishmentLog);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",replenishmentLog),HttpStatus.OK);
    }


    @DeleteMapping("/dinedynamo/restaurant/inventory/deletel-replenishment-logs-for-restaurant")
    public ResponseEntity<ApiResponse> deleteAllReplenishmentLog(@RequestBody Restaurant restaurant){


        restaurant = restaurantRepository.findById(restaurant.getRestaurantId()).orElse(null);

        if(restaurant==null){
            System.out.println("RESTAURANT NOT FOUND IN DB, ID INCORRECT");
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND,"success",null),HttpStatus.OK);

        }

        List<ReplenishmentLog> replenishmentLogList = replenishmentLogRepository.deleteByRestaurantId(restaurant.getRestaurantId());
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",replenishmentLogList),HttpStatus.OK);
    }

    @DeleteMapping("/dinedynamo/restaurant/inventory/delete-replenishment-logs-for-raw-material")
    public ResponseEntity<ApiResponse> deleteAllReplenishmentLogsForRawMaterial(@RequestBody  RawMaterial rawMaterial){

        rawMaterial = rawMaterialRepository.findById(rawMaterial.getRawMaterialId()).orElse(null);

        rawMaterial = rawMaterialRepository.findById(rawMaterial.getRawMaterialId()).orElse(null);

        if(rawMaterial==null){
            System.out.println("RAW MATERIAL NOT FOUND IN DB, ID INCORRECT");
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND,"success",null),HttpStatus.OK);

        }

        else{
            List<ReplenishmentLog> replenishmentLogList = replenishmentLogRepository.deleteByRawMaterialId(rawMaterial.getRawMaterialId());
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",replenishmentLogList),HttpStatus.OK);
        }

    }


}
