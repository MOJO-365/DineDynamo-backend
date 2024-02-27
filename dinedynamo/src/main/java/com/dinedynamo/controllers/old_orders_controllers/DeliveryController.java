package com.dinedynamo.controllers.old_orders_controllers;

import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.collections.invoice_collections.DineInFinalBill;
import com.dinedynamo.collections.old_order_collections.DeliveryOrder;
import com.dinedynamo.repositories.old_orders_repositories.DeliveryOrderRepository;
import com.dinedynamo.repositories.invoice_repositories.DineInBillRepository;
import com.dinedynamo.repositories.restaurant_repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
public class DeliveryController {

    @Autowired
    private DeliveryOrderRepository deliveryOrderRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private DineInBillRepository dineInBillRepository;


    @PostMapping("/dinedynamo/restaurant/orders/delivery")
    public ResponseEntity<ApiResponse> createDeliveryOrder(@RequestBody DeliveryOrder deliveryOrder) {
        // Step 1: Process the delivery order
        DeliveryOrder savedOrder = deliveryOrderRepository.save(deliveryOrder);

        // Step 2: Prepare data for the Final Bill
        String restaurantId = deliveryOrder.getRestaurantId(); // Get restaurant ID from the delivery order
        String deliveryId = savedOrder.getDeliveryId(); // Get delivery ID from the saved delivery order
        // Additional data preparation as needed...

        // Step 3: Create Final Bill object
        DineInFinalBill dineInFinalBill = new DineInFinalBill();
        dineInFinalBill.setRestaurantId(restaurantId);
        dineInFinalBill.setOrderList(deliveryOrder.getOrderList());
        // Set other relevant fields for the Final Bill

        // Step 4: Save the Final Bill
        dineInBillRepository.save(dineInFinalBill);

        // Prepare and return the response
        ApiResponse response = new ApiResponse(HttpStatus.OK, "success", dineInFinalBill);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("dinedynami/restaurant/order/get-final-bill")
    public ResponseEntity<ApiResponse> getfinal(@RequestBody Restaurant restaurant){

        restaurant= restaurantRepository.findById(restaurant.getRestaurantId()).orElse(null);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",null),HttpStatus.OK);
    }

    @PostMapping("/dinedynamo/restaurant/orders/getDeliveryorder")
    public ResponseEntity<ApiResponse> getAllOrders(@RequestBody Restaurant restaurant){


        restaurant = restaurantRepository.findById(restaurant.getRestaurantId()).orElse(null);


        if (restaurant == null)
        {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "success", null), HttpStatus.NOT_FOUND);
        }
        else
        {
            List<DeliveryOrder> orders = deliveryOrderRepository.findByRestaurantId(restaurant.getRestaurantId());

            return new ResponseEntity<ApiResponse>(new ApiResponse(HttpStatus.OK, "success", orders), HttpStatus.OK);

        }
    }



    @PostMapping("/dinedynamo/restaurant/delivery/updateorder")
    public ResponseEntity<ApiResponse> updateOrder( @RequestBody DeliveryOrder deliveryOrder) {

            DeliveryOrder existingOrder = deliveryOrderRepository.findById(deliveryOrder.getDeliveryId()).orElse(null);

            if (existingOrder != null) {

                existingOrder.setOrderList(deliveryOrder.getOrderList());

                deliveryOrderRepository.save(existingOrder);
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", null), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "failure", null), HttpStatus.NOT_FOUND);

            }
    }



    @DeleteMapping("/dinedynamo/restaurant/delivery/deleteorder")
    public ResponseEntity<ApiResponse> deleteOrder(@RequestBody DeliveryOrder deliveryOrder) {
        Optional<DeliveryOrder> deleteOrder = deliveryOrderRepository.findById(deliveryOrder.getDeliveryId());

        if (deleteOrder.isPresent()) {
            deliveryOrderRepository.deleteById(deliveryOrder.getDeliveryId());
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", null), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "failure", null), HttpStatus.NOT_FOUND);
        }
    }


}
