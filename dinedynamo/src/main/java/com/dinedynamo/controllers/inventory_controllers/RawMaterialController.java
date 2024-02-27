package com.dinedynamo.controllers.inventory_controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.inventory_management.RawMaterial;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.dto.inventory_dtos.EditRawMaterialDTO;
import com.dinedynamo.repositories.inventory_repositories.RawMaterialRepository;
import com.dinedynamo.services.inventory_service.RawMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class RawMaterialController {

    @Autowired
    RawMaterialRepository rawMaterialRepository;

    @Autowired
    RawMaterialService rawMaterialService;

    @PostMapping("/dinedynamo/restaurant/inventory/save-raw-material")
    ResponseEntity<ApiResponse> saveRawMaterial(@RequestBody RawMaterial rawMaterial){

        rawMaterialRepository.save(rawMaterial);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",rawMaterial),HttpStatus.OK);
    }

    @PostMapping("/dinedynamo/restaurant/inventory/get-all-raw-materials")
    ResponseEntity<ApiResponse> findAllRawMaterialsOfRestaurant(@RequestBody Restaurant restaurant){

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",rawMaterialRepository.findByRestaurantId(restaurant.getRestaurantId())),HttpStatus.OK);
    }

    @PutMapping("/dinedynamo/restaurant/inventory/edit-raw-materials")
    ResponseEntity<ApiResponse> editRawMaterial(@RequestBody EditRawMaterialDTO editRawMaterialDTO){

        RawMaterial updatedRawMaterial = rawMaterialService.updateRawMaterial(editRawMaterialDTO.getRawMaterialId(), editRawMaterialDTO.getRawMaterial());
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",updatedRawMaterial),HttpStatus.OK);

    }

    @DeleteMapping("/dinedynamo/restaurant/inventory/delete-raw-material")
    ResponseEntity<ApiResponse> deleteRawMaterial(@RequestBody RawMaterial rawMaterial){

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",rawMaterialService.deleteRawMaterial(rawMaterial)),HttpStatus.OK);
    }

    @DeleteMapping("/dinedynamo/restaurant/inventory/delete-all-raw-materials")
    ResponseEntity<ApiResponse> deleteAllRawMaterials(@RequestBody Restaurant restaurant){
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",rawMaterialService.deleteAllRawMaterials(restaurant)),HttpStatus.OK);
    }

    @PutMapping("/dinedynamo/restaurant/inventory/add-usage")
    ResponseEntity<ApiResponse> addUsage(@RequestParam double amountUsed, @RequestBody RawMaterial rawMaterial){

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",rawMaterialService.addUsage(rawMaterial, amountUsed)), HttpStatus.OK);

    }
}
