package com.dinedynamo.controllers;

import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.DeliveryOrder;
import com.dinedynamo.collections.Order;
import com.dinedynamo.collections.Restaurant;
import com.dinedynamo.repositories.DeliveryOrderRepository;
import com.dinedynamo.repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
public class DeliveryController {

    @Autowired
    private DeliveryOrderRepository deliveryOrderRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;


    @PostMapping("/dinedynamo/restaurant/orders/delivery")
    public ResponseEntity<ApiResponse> createDeliveryOrder(@RequestBody DeliveryOrder deliveryOrder) {

            DeliveryOrder savedOrder = deliveryOrderRepository.save(deliveryOrder);
            ApiResponse response = new ApiResponse(HttpStatus.OK, "success", savedOrder);
            return new ResponseEntity<>(response, HttpStatus.OK);

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
        try {
            DeliveryOrder existingOrder = deliveryOrderRepository.findById(deliveryOrder.getDeliveryId()).orElse(null);

            if (existingOrder != null) {

                existingOrder.setOrderList(deliveryOrder.getOrderList());

                deliveryOrderRepository.save(existingOrder);
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", null), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "failure", null), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "failure", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
