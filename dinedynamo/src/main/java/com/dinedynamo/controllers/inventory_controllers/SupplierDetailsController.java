package com.dinedynamo.controllers.inventory_controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.inventory_management.RawMaterial;
import com.dinedynamo.collections.inventory_management.SupplierDetails;
import com.dinedynamo.collections.restaurant_collections.Restaurant;

import com.dinedynamo.dto.inventory_dtos.AddMultipleItemsSuppliersDTO;
import com.dinedynamo.dto.inventory_dtos.EditSupplierDetailsDTO;
import com.dinedynamo.repositories.inventory_repositories.RawMaterialRepository;
import com.dinedynamo.repositories.inventory_repositories.SupplierDetailsRepository;
import com.dinedynamo.repositories.restaurant_repositories.RestaurantRepository;
import com.dinedynamo.services.inventory_services.SupplierDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
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
    ResponseEntity<ApiResponse> addMultipleItemsSuppliers(@RequestBody AddMultipleItemsSuppliersDTO addAllSuppliersDTO){

        supplierDetailsService.addSupplierForMultipleItems(addAllSuppliersDTO);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",true), HttpStatus.OK);

    }


    @PostMapping("/dinedynamo/restaurant/inventory/suppliers/add-supplier")
    ResponseEntity<ApiResponse> addSupplierForRawMaterial(@RequestBody SupplierDetails supplierDetails){

        supplierDetails = supplierDetailsService.save(supplierDetails);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",supplierDetails), HttpStatus.OK);
    }

    @DeleteMapping("/dinedynamo/restaurant/inventory/suppliers/delete-supplier")
    ResponseEntity<ApiResponse> deleteSupplier(@RequestBody SupplierDetails supplierDetails){

        supplierDetailsRepository.delete(supplierDetails);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",supplierDetails), HttpStatus.OK);
    }

    @PutMapping("/dinedynamo/restaurant/inventory/suppliers/edit-supplier")
    ResponseEntity<ApiResponse> editSupplier(@RequestBody EditSupplierDetailsDTO editSupplierDetailsDTO){

        SupplierDetails updatedupplierDetails = supplierDetailsService.updateSupplierDetails(editSupplierDetailsDTO.getSupplierId(),editSupplierDetailsDTO.getSupplierDetails());
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",updatedupplierDetails), HttpStatus.OK);

    }

    @PostMapping("/dinedynamo/restaurant/inventory/suppliers/get-suppliers-for-restaurant")
    ResponseEntity<ApiResponse> getSuppliersForRestaurant(@RequestBody Restaurant restaurant){
        List<SupplierDetails> supplierDetailsList = supplierDetailsRepository.findByRestaurantId(restaurant.getRestaurantId());
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",supplierDetailsList), HttpStatus.OK);
    }

    @PostMapping("/dinedynamo/restaurant/inventory/suppliers/get-suppliers-for-raw-material")
    ResponseEntity<ApiResponse> getSuppliersForRwaMaterial(@RequestBody RawMaterial rawMaterial){

        rawMaterial = rawMaterialRepository.findById(rawMaterial.getRawMaterialId()).orElse(null);
        if(rawMaterial == null){
            System.out.println("RAW MATERIAL ID NOT FOUND IN DB");
        }
        List<SupplierDetails> supplierDetailsList = supplierDetailsRepository.findByRawMaterialId(rawMaterial.getRawMaterialId());
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",supplierDetailsList), HttpStatus.OK);

    }


    @DeleteMapping("/dinedynamo/restaurant/inventory/suppliers/delete-all-suppliers-for-raw-material")
    ResponseEntity<ApiResponse> deleteSuppliersForRawMaterial(@RequestBody RawMaterial rawMaterial){
        rawMaterial = rawMaterialRepository.findById(rawMaterial.getRawMaterialId()).orElse(null);
        if(rawMaterial == null){
            System.out.println("RAW MATERIAL ID NOT FOUND IN DB");
        }

        List<SupplierDetails> supplierDetailsList = supplierDetailsRepository.deleteByRawMaterialId(rawMaterial.getRawMaterialId());
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",supplierDetailsList), HttpStatus.OK);
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
