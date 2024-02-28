package com.dinedynamo.controllers.inventory_controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.inventory_management.SupplierDetails;
import com.dinedynamo.dto.inventory_dtos.EditSupplierDetailsDTO;
import com.dinedynamo.repositories.inventory_repositories.SupplierDetailsRepository;
import com.dinedynamo.services.inventory_service.SupplierDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class SupplierDetailsController {


    @Autowired
    SupplierDetailsRepository supplierDetailsRepository;

    @Autowired
    SupplierDetailsService supplierDetailsService;

    @PostMapping("/dinedynamo/restaurant/inventory/supplier/add-supplier")
    ResponseEntity<ApiResponse> addSupplierForRawMaterial(@RequestBody SupplierDetails supplierDetails){

        supplierDetails = supplierDetailsService.save(supplierDetails);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",supplierDetails), HttpStatus.OK);
    }

    @DeleteMapping("/dinedynamo/restaurant/inventory/supplier/delete-supplier")
    ResponseEntity<ApiResponse> deleteSupplier(@RequestBody SupplierDetails supplierDetails){

        supplierDetailsRepository.delete(supplierDetails);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",supplierDetails), HttpStatus.OK);
    }




}
