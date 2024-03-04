package com.dinedynamo.controllers.inventory_controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.inventory_management.RawMaterial;
import com.dinedynamo.collections.inventory_management.WastageLog;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.repositories.inventory_repositories.WastageLogRepository;
import com.dinedynamo.services.inventory_services.WastageLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class WastageLogController {


    @Autowired
    WastageLogRepository wastageLogRepository;

    @Autowired
    WastageLogService wastageLogService;

    @PostMapping("/dinedynamo/restaurant/inventory/add-wastage-log")
    public ResponseEntity<ApiResponse> addWastageLog(@RequestBody WastageLog wastageLog){

       return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",wastageLogService.save(wastageLog)),HttpStatus.OK);

    }

    @PostMapping("/dinedynamo/restaurant/inventory/get-all-wastage-logs-for-raw-material")
    public ResponseEntity<ApiResponse> getAllWastageLogs(@RequestBody RawMaterial rawMaterial){

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",wastageLogRepository.findByRawMaterialId(rawMaterial.getRawMaterialId())),HttpStatus.OK);
    }

//    @PutMapping("/dinedynamo/restaurant/inventory/edit-wastage-log")
//    public ResponseEntity<ApiResponse> editWastageLog(@RequestBody EditWastageLogDTO editWastageLogDTO){
//
//        WastageLog updatedWastageLog = wastageLogService.updateWastageLog(editWastageLogDTO.getWastageLogId(), editWastageLogDTO.getWastageLog());
//        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",updatedWastageLog),HttpStatus.OK);
//
//    }

    @DeleteMapping("/dinedynamo/restaurant/inventory/delete-wastage-log")
    public ResponseEntity<ApiResponse> deleteWastageLog(@RequestBody WastageLog wastageLog){

        wastageLogRepository.delete(wastageLog);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",wastageLog),HttpStatus.OK);
    }

    @DeleteMapping("/dinedynamo/restaurant/inventory/delete-all-wastage-logs-for-raw-material")
    public ResponseEntity<ApiResponse> deleteAllWastageLogsForRawMaterial(@RequestBody RawMaterial rawMaterial){
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",wastageLogRepository.deleteByRawMaterialId(rawMaterial.getRawMaterialId())),HttpStatus.OK);

    }


    @DeleteMapping("/dinedynamo/restaurant/inventory/delete-all-wastage-logs-for-restaurant")
    public ResponseEntity<ApiResponse> deleteAllWastageLogsForRestaurant(@RequestBody Restaurant restaurant){
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",wastageLogRepository.deleteByRestaurantId(restaurant.getRestaurantId())),HttpStatus.OK);
    }
}
