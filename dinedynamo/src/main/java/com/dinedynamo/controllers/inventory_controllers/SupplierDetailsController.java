package com.dinedynamo.controllers.inventory_controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.inventory_management.SupplierDetails;
import com.dinedynamo.repositories.inventory_repositories.SupplierDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class SupplierDetailsController {


    @Autowired
    SupplierDetailsRepository supplierDetailsRepository;

    @PostMapping("/dinedynamo/restaurant/inventory/supplier/add-supplier")
    ResponseEntity<ApiResponse> addSupplierForRawMaterial(@RequestBody SupplierDetails supplierDetails){

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",supplierDetails), HttpStatus.OK);
    }




}
