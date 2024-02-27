package com.dinedynamo.controllers.invoice_controllers;

import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.invoice_collections.DeliveryFinalBill;
import com.dinedynamo.services.invoice_services.DeliveryBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeliveryBillController {
    @Autowired
    private  DeliveryBillService deliveryBillService;

    @PostMapping("/dinedynamo/restaurant/orders/save/delivery-final-bill")
    public ResponseEntity<ApiResponse> saveDeliveryBill(@RequestBody DeliveryFinalBill deliveryFinalBill) {
        deliveryBillService.saveDeliveryBill(deliveryFinalBill);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Data Stored", deliveryFinalBill), HttpStatus.OK);
    }
}
