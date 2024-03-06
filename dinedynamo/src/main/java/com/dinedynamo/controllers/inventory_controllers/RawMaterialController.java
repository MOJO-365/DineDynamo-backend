package com.dinedynamo.controllers.inventory_controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.inventory_management.RawMaterial;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.dto.inventory_dtos.AddUsageForRawMaterialDTO;
import com.dinedynamo.dto.inventory_dtos.EditRawMaterialDTO;
import com.dinedynamo.dto.inventory_dtos.RawMaterialDTO;
import com.dinedynamo.dto.inventory_dtos.RawMaterialStatus;
import com.dinedynamo.repositories.inventory_repositories.RawMaterialRepository;
import com.dinedynamo.services.inventory_services.RawMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@RestController
@CrossOrigin("*")
public class RawMaterialController {

    @Autowired
    RawMaterialRepository rawMaterialRepository;

    @Autowired
    RawMaterialService rawMaterialService;

    @PostMapping("/dinedynamo/restaurant/inventory/add-raw-material")
    ResponseEntity<ApiResponse> addRawMaterial(@RequestBody RawMaterial rawMaterial){
        RawMaterialDTO rawMaterialDTO = rawMaterialService.save(rawMaterial);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",rawMaterialDTO),HttpStatus.OK);
    }

    @PostMapping("/dinedynamo/restaurant/inventory/get-all-raw-materials")
    ResponseEntity<ApiResponse> findAllRawMaterialsOfRestaurant(@RequestBody Restaurant restaurant){
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",rawMaterialRepository.findByRestaurantId(restaurant.getRestaurantId())),HttpStatus.OK);
    }

    @PostMapping("/dinedynamo/restaurant/inventory/get-raw-material-by-id")
    ResponseEntity<ApiResponse> findRawMaterialById(@RequestParam String rawMaterialId){

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",rawMaterialRepository.findById(rawMaterialId)),HttpStatus.OK);

    }

    @PutMapping("/dinedynamo/restaurant/inventory/edit-raw-material")
    ResponseEntity<ApiResponse> editRawMaterial(@RequestBody EditRawMaterialDTO editRawMaterialDTO){

        RawMaterialDTO rawMaterialDTO = rawMaterialService.updateRawMaterial(editRawMaterialDTO.getRawMaterialId(), editRawMaterialDTO.getRawMaterial());
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",rawMaterialDTO),HttpStatus.OK);

    }


    @DeleteMapping("/dinedynamo/restaurant/inventory/delete-raw-material")
    ResponseEntity<ApiResponse> deleteRawMaterial(@RequestBody RawMaterial rawMaterial){
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",rawMaterialService.deleteRawMaterial(rawMaterial)),HttpStatus.OK);
    }

    @DeleteMapping("/dinedynamo/restaurant/inventory/delete-all-raw-materials")
    ResponseEntity<ApiResponse> deleteAllRawMaterials(@RequestBody Restaurant restaurant){
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",rawMaterialService.deleteAllRawMaterials(restaurant)),HttpStatus.OK);
    }

    //Used to change the current level of raw material
    // If returns true -> currentLevel updated
    // If returns false -> currentLevel not updated
    @PutMapping("/dinedynamo/restaurant/inventory/add-usage")
    ResponseEntity<ApiResponse> addUsage(@RequestBody AddUsageForRawMaterialDTO addUsageForRawMaterialDTO){
        RawMaterialDTO rawMaterialDTO = rawMaterialService.addUsage(addUsageForRawMaterialDTO.getRawMaterialId(), addUsageForRawMaterialDTO.getAmountUsed());

        if(rawMaterialDTO.getStatus() == RawMaterialStatus.NEGATIVE){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",false), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",true), HttpStatus.OK);
        }
    }

    @PostMapping("/dinedynamo/restaurant/inventory/search-by-category")
    ResponseEntity<ApiResponse> findByRestaurantIdAndCategory(@RequestParam String restaurantId, @RequestParam String category){
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",rawMaterialRepository.findByRestaurantIdAndCategory(restaurantId, category)), HttpStatus.OK);
    }

    @PostMapping("/dinedynamo/restaurant/inventory/search-by-name")
    ResponseEntity<ApiResponse> findByRestaurantIdAndName(@RequestParam String restaurantId, @RequestParam String name){
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",rawMaterialRepository.findByRestaurantIdAndName(restaurantId, name)), HttpStatus.OK);
    }

    @PostMapping("/dinedynamo/restaurant/inventory/update-all-raw-material-status")
    ResponseEntity<ApiResponse> updateStatusOfRawMaterials(@RequestBody Restaurant restaurant){
        List<RawMaterial> rawMaterialList = rawMaterialService.updateStatusOfAllRawMaterials(restaurant);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",rawMaterialList), HttpStatus.OK);
    }

    @PostMapping("/dinedynamo/restaurant/inventory/get-critical-raw-materials")
    ResponseEntity<ApiResponse> getAllCriticalRawMaterials(@RequestBody Restaurant restaurant){
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",rawMaterialRepository.findByRestaurantIdAndStatus(restaurant.getRestaurantId(), com.dinedynamo.collections.inventory_management.RawMaterialStatus.CRITICAL)), HttpStatus.OK);
    }

    @PostMapping("/dinedynamo/restaurant/inventory/get-near-reorder-raw-materials")
    ResponseEntity<ApiResponse> getAllNearReorderRawMaterials(@RequestBody Restaurant restaurant){
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",rawMaterialRepository.findByRestaurantIdAndStatus(restaurant.getRestaurantId(), com.dinedynamo.collections.inventory_management.RawMaterialStatus.NEAR_REORDER)), HttpStatus.OK);
    }

    @PostMapping("/dinedynamo/restaurant/inventory/get-sufficient-raw-materials")
    ResponseEntity<ApiResponse> getAllSufficientRawMaterials(@RequestBody Restaurant restaurant){
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",rawMaterialRepository.findByRestaurantIdAndStatus(restaurant.getRestaurantId(), com.dinedynamo.collections.inventory_management.RawMaterialStatus.SUFFICIENT)), HttpStatus.OK);
    }

    @PostMapping("/dinedynamo/restaurant/inventory/get-categories")
    ResponseEntity<ApiResponse> getAllCategoriesForRestaurant(@RequestBody Restaurant restaurant){

        HashSet<String> categories = new HashSet<>();
        List<RawMaterial> rawMaterialList = rawMaterialRepository.findByRestaurantId(restaurant.getRestaurantId());

        for(RawMaterial rawMaterial: rawMaterialList){
            categories.add(rawMaterial.getCategory());
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",categories), HttpStatus.OK);
    }

    @PostMapping("/dinedynamo/restaurant/inventory/get-measurement-units")
    ResponseEntity<ApiResponse> getAllMeasurementUnitsForRestaurant(@RequestBody Restaurant restaurant){

        HashSet<String> measurementUnits = new HashSet<>();
        List<RawMaterial> rawMaterialList = rawMaterialRepository.findByRestaurantId(restaurant.getRestaurantId());

        for(RawMaterial rawMaterial: rawMaterialList){
            measurementUnits.add(rawMaterial.getMeasurementUnits());
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",measurementUnits), HttpStatus.OK);
    }

    @PostMapping("/dinedynamo/restaurant/inventory/get-expired-raw-materials")
    ResponseEntity<ApiResponse> getExpiredRawMaterials(@RequestBody Restaurant restaurant){
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",rawMaterialRepository.findByRestaurantIdAndExpirationDateBefore(restaurant.getRestaurantId(),LocalDate.now())), HttpStatus.OK);
    }

    @PostMapping("/dinedynamo/restaurant/inventory/sort-by-expiration-date")
    ResponseEntity<ApiResponse> sortByExpirationDate(@RequestBody Restaurant restaurant){
        Sort sortByExpirationDateAndTimestamp = Sort.by(Sort.Direction.ASC, "expirationDate").and(Sort.by(Sort.Direction.DESC, "timestamp"));
        List<RawMaterial> sortedRawMaterials = rawMaterialRepository.findByRestaurantId(restaurant.getRestaurantId(), sortByExpirationDateAndTimestamp);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",sortedRawMaterials), HttpStatus.OK);
    }

    @PostMapping("/dinedynamo/restaurant/inventory/sort-by-timestamp")
    ResponseEntity<ApiResponse> sortByTimeStamp(@RequestBody Restaurant restaurant){
        Sort sortByTimestamp = Sort.by(Sort.Direction.DESC, "timestamp");
        List<RawMaterial> sortedRawMaterials = rawMaterialRepository.findByRestaurantId(restaurant.getRestaurantId(), sortByTimestamp);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",sortedRawMaterials), HttpStatus.OK);
    }

    @PostMapping("/dinedynamo/restaurant/inventory/sort-by-category")
    ResponseEntity<ApiResponse> sortByCategory(@RequestBody Restaurant restaurant){
        Sort sortByCategoryAndTimestamp = Sort.by(Sort.Direction.ASC, "category").and(Sort.by(Sort.Direction.DESC, "timestamp"));
        List<RawMaterial> sortedRawMaterials = rawMaterialRepository.findByRestaurantId(restaurant.getRestaurantId(), sortByCategoryAndTimestamp);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",sortedRawMaterials), HttpStatus.OK);
    }


    @PostMapping("/dinedynamo/restaurant/inventory/sort-by-name")
    ResponseEntity<ApiResponse> sortByName(@RequestBody Restaurant restaurant){
        Sort sortByNameAndTimestamp = Sort.by(Sort.Direction.ASC, "name").and(Sort.by(Sort.Direction.ASC, "timestamp"));
        List<RawMaterial> sortedRawMaterials = rawMaterialRepository.findByRestaurantId(restaurant.getRestaurantId(), sortByNameAndTimestamp);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",sortedRawMaterials), HttpStatus.OK);
    }



}
