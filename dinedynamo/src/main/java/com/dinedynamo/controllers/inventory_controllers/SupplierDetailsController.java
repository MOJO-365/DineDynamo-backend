package com.dinedynamo.controllers.inventory_controllers;

import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.inventory_management.RawMaterial;
import com.dinedynamo.collections.inventory_management.SupplierDetails;
import com.dinedynamo.collections.restaurant_collections.Restaurant;

import com.dinedynamo.dto.inventory_dtos.EditSupplierDetailsDTO;
import com.dinedynamo.repositories.inventory_repositories.RawMaterialRepository;
import com.dinedynamo.repositories.inventory_repositories.SupplierDetailsRepository;
import com.dinedynamo.repositories.restaurant_repositories.RestaurantRepository;
import com.dinedynamo.services.inventory_services.SupplierDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@Slf4j
public class SupplierDetailsController {


    @Autowired
    SupplierDetailsRepository supplierDetailsRepository;

    @Autowired
    SupplierDetailsService supplierDetailsService;

    @Autowired
    RawMaterialRepository rawMaterialRepository;

    @Autowired
    RestaurantRepository restaurantRepository;



    @PostMapping("/dinedynamo/restaurant/inventory/suppliers/add-supplier-for-items")
    ResponseEntity<ApiResponse> addMultipleItemsSuppliers(@RequestBody SupplierDetails supplierDetails){

        supplierDetailsService.save(supplierDetails);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",supplierDetails), HttpStatus.OK);

    }


//    @PostMapping("/dinedynamo/restaurant/inventory/suppliers/add-supplier")
//    ResponseEntity<ApiResponse> addSupplierForRawMaterial(@RequestBody SupplierDetails supplierDetails){
//
//        supplierDetails = supplierDetailsService.save(supplierDetails);
//        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",supplierDetails), HttpStatus.OK);
//    }

    @DeleteMapping("/dinedynamo/restaurant/inventory/suppliers/delete-supplier")
    ResponseEntity<ApiResponse> deleteSupplier(@RequestBody SupplierDetails supplierDetails){

        supplierDetailsRepository.delete(supplierDetails);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",supplierDetails), HttpStatus.OK);
    }

    @PutMapping("/dinedynamo/restaurant/inventory/suppliers/edit-supplier")
    ResponseEntity<ApiResponse> editSupplier(@RequestBody EditSupplierDetailsDTO editSupplierDetailsDTO){

        SupplierDetails updatedSupplierDetails = supplierDetailsService.updateSupplierDetails(editSupplierDetailsDTO.getSupplierId(),editSupplierDetailsDTO.getSupplierDetails());
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",updatedSupplierDetails), HttpStatus.OK);

    }

    @PostMapping("/dinedynamo/restaurant/inventory/suppliers/get-suppliers-for-restaurant")
    ResponseEntity<ApiResponse> getSuppliersForRestaurant(@RequestBody Restaurant restaurant){
        List<SupplierDetails> supplierDetailsList = supplierDetailsRepository.findByRestaurantId(restaurant.getRestaurantId());
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",supplierDetailsList), HttpStatus.OK);
    }

    @PostMapping("/dinedynamo/restaurant/inventory/suppliers/get-items-by-supplier")
    ResponseEntity<ApiResponse> getItemsBySupplier(@RequestBody SupplierDetails supplierDetails){

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",supplierDetailsService.getItemsBySupplier(supplierDetails)), HttpStatus.OK);

    }

    @PostMapping("/dinedynamo/restaurant/inventory/suppliers/get-supplier-for-raw-material")
    ResponseEntity<ApiResponse> getItemsBySupplier(@RequestBody RawMaterial rawMaterial){


        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",supplierDetailsService.getSupplierByRawMaterial(rawMaterial)), HttpStatus.OK);

    }


    @DeleteMapping("/dinedynamo/restaurant/inventory/suppliers/delete-all-suppliers-for-restaurant")
    ResponseEntity<ApiResponse> deleteSuppliersForRawMaterial(@RequestBody Restaurant restaurant){
        restaurant = restaurantRepository.findById(restaurant.getRestaurantId()).orElse(null);
        if(restaurant == null){
            System.out.println("RESTAURANT ID NOT FOUND IN DB");
        }

        List<SupplierDetails> supplierDetailsList = supplierDetailsRepository.deleteByRestaurantId(restaurant.getRestaurantId());
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",supplierDetailsList), HttpStatus.OK);
    }


    @PostMapping("/dinedynamo/restaurant/inventory/suppliers/search-by-name")
    ResponseEntity<ApiResponse> searchSupplierByName(@RequestParam String restaurantId, @RequestParam String supplierName){

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",supplierDetailsRepository.findByRestaurantIdAndSupplierName(restaurantId, supplierName)), HttpStatus.OK);

    }


}
