package com.dinedynamo.controllers.new_orders_controllers;

import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.collections.order_collections.DeliveryOrder;
import com.dinedynamo.repositories.order_repositories.NewDeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
public class NewDeliveryController {

    @Autowired
    private NewDeliveryRepository deliveryOrderRepository;

    // for Creating delivery order
    @PostMapping("/dinedynamo/restaurant/orders/delivery/create")
    public ResponseEntity<ApiResponse> createDeliveryOrder(@RequestBody DeliveryOrder deliveryOrder) {
        deliveryOrder.setDateTime(LocalDateTime.now());
        deliveryOrder.setDeliveryStatus(false);
        DeliveryOrder savedOrder = deliveryOrderRepository.save(deliveryOrder);
        ApiResponse response = new ApiResponse(HttpStatus.OK, "Success", savedOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // Get all delivery orders for a restaurant using restaurantId
    @PostMapping("/dinedynamo/restaurant/orders/delivery/getAll")
    public ResponseEntity<ApiResponse> getAllDeliveryOrders(@RequestBody Restaurant restaurant) {
        String restaurantId = restaurant.getRestaurantId();
        List<DeliveryOrder> orders = deliveryOrderRepository.findByRestaurantId(restaurantId);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Success", orders), HttpStatus.OK);
    }
    @PostMapping("/dinedynamo/restaurant/delivery/update")
    public ResponseEntity<ApiResponse> updateDeliveryOrder(@RequestBody DeliveryOrder updatedDeliveryOrder) {
        Optional<DeliveryOrder> existingDeliveryOrderOptional = deliveryOrderRepository.findById(updatedDeliveryOrder.getDeliveryId());

        if (existingDeliveryOrderOptional.isPresent()) {
            DeliveryOrder existingDeliveryOrder = existingDeliveryOrderOptional.get();

            existingDeliveryOrder.setDeliveryStatus(true);
            existingDeliveryOrder.setOrderList(updatedDeliveryOrder.getOrderList());

            deliveryOrderRepository.save(existingDeliveryOrder);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "Delivery order updated successfully", null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(HttpStatus.NOT_FOUND, "Delivery order not found", null));
        }
    }


    // Delete delivery order using deliveryId
    @DeleteMapping("/dinedynamo/restaurant/orders/delivery/delete")
    public ResponseEntity<ApiResponse> deleteDeliveryOrder(@RequestBody DeliveryOrder deliveryOrder) {
        Optional<DeliveryOrder> deleteOrder = deliveryOrderRepository.findById(deliveryOrder.getDeliveryId());

        if (deleteOrder.isPresent()) {
            deliveryOrderRepository.deleteById(deliveryOrder.getDeliveryId());
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Success", null), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "Delivery order not found", null), HttpStatus.NOT_FOUND);
        }
    }
}
