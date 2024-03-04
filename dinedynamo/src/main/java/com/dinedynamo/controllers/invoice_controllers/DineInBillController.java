package com.dinedynamo.controllers.invoice_controllers;

import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.invoice_collections.DineInFinalBill;
import com.dinedynamo.services.invoice_services.DineInBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class DineInBillController {

    @Autowired
    private  DineInBillService dineInBillService;

    @PostMapping("/dinedynamo/restaurant/orders/save/dinein-final-bill")
    public ResponseEntity<ApiResponse> saveDineInBill(@RequestBody DineInFinalBill dineInFinalBill) {
        dineInFinalBill.setDatetime(LocalDateTime.now());
        dineInBillService.saveDineInBill(dineInFinalBill);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Data Stored", dineInFinalBill), HttpStatus.OK);
    }
}
