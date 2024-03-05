package com.dinedynamo.controllers.inventory_controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.inventory_management.PurchaseOrder;
import com.dinedynamo.collections.inventory_management.PurchaseOrderStatus;
import com.dinedynamo.collections.inventory_management.RawMaterial;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.repositories.inventory_repositories.PurchaseOrderRepository;
import com.dinedynamo.services.inventory_services.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class PurchaseOrderController
{
    @Autowired
    PurchaseOrderService purchaseOrderService;

    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;


    //Use: To send the Purchase order mail to Supplier
    @PostMapping("/dinedynamo/restaurant/inventory/purchase-order-request")
    public ResponseEntity<ApiResponse> raiseTicketForPurchaseOrder(@RequestBody PurchaseOrder purchaseOrder){

        purchaseOrder = purchaseOrderService.save(purchaseOrder);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",purchaseOrder),HttpStatus.OK);

    }

    // Use: When the restaurant owner has received the item, the status of that purchase order needs to be updated in DB
    @PostMapping("/dinedynamo/restaurant/inventory/complete-purchase-order")
    public ResponseEntity<ApiResponse> completePurchaseOrder(@RequestBody PurchaseOrder purchaseOrder){

        purchaseOrder = purchaseOrderService.completePurchaseOrder(purchaseOrder);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",purchaseOrder),HttpStatus.OK);

    }

    //Use: If the restaurant owner wants to cancel the order, mail will be sent to the supplier of that particular raw material
    @PostMapping("/dinedynamo/restaurant/inventory/cancel-purchase-order")
    public ResponseEntity<ApiResponse> cancelPurchaseOrder(@RequestBody PurchaseOrder purchaseOrder){

        purchaseOrder = purchaseOrderService.completePurchaseOrder(purchaseOrder);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",purchaseOrder),HttpStatus.OK);

    }


    @DeleteMapping("/dinedynamo/restaurant/inventory/delete-purchase-order")
    public ResponseEntity<ApiResponse> deletePurchaseOrder(@RequestBody PurchaseOrder purchaseOrder){

        purchaseOrderRepository.delete(purchaseOrder);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",purchaseOrder),HttpStatus.OK);
    }


    @PostMapping("/dinedynamo/restaurant/inventory/get-all-purchase-orders-for-raw-material")
    public ResponseEntity<ApiResponse> getPurchaseOrdersForRawMaterial(@RequestBody RawMaterial rawMaterial){

        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findByRawMaterialId(rawMaterial.getRawMaterialId());
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",purchaseOrderList),HttpStatus.OK);

    }

    @PostMapping("/dinedynamo/restaurant/inventory/get-all-purchase-orders-for-restaurant")
    public ResponseEntity<ApiResponse> getPurchaseOrdersForRestaurant(@RequestBody Restaurant restaurant){

        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findByRestaurantId(restaurant.getRestaurantId());
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",purchaseOrderList),HttpStatus.OK);

    }





}
