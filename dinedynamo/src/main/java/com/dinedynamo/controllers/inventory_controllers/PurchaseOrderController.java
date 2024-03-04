package com.dinedynamo.controllers.inventory_controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.inventory_management.PurchaseOrder;
import com.dinedynamo.services.inventory_services.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class PurchaseOrderController
{
    @Autowired
    PurchaseOrderService purchaseOrderService;


    @PostMapping("/dinedynamo/restaurant/inventory/purchase-order")
    public ResponseEntity<ApiResponse> raiseTicketForPurchaseOrder(@RequestBody PurchaseOrder purchaseOrder){

        purchaseOrder = purchaseOrderService.save(purchaseOrder);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",purchaseOrder),HttpStatus.OK);

    }


}
