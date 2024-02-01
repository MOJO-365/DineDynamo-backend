package com.dinedynamo.controllers;

import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.*;
import com.dinedynamo.repositories.OrderRepository;
import com.dinedynamo.repositories.RestaurantRepository;
import com.dinedynamo.repositories.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dinedynamo.repositories.CustomerRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;



@RestController
@CrossOrigin("*")

public class OrderController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TableRepository tableRepository;


    //for placing order
    @PostMapping("/dinedynamo/restaurant/orders/placeorder")
    public ResponseEntity<ApiResponse> placeOrder(@RequestBody Order order) {

        Order existingOrder = orderRepository.findByTableId(order.getTableId()).orElse(null);



        if(existingOrder == null){
            orderRepository.save(order);
        }

        else{
            order.setOrderId(existingOrder.getOrderId());
            orderRepository.delete(existingOrder);
            orderRepository.save(order);
        }



        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Success", order), HttpStatus.OK);
    }


    //Order prepared status true or false
    @GetMapping("/dinedynamo/order/prepare")
    public ResponseEntity<Object> getOrder(@RequestBody Order order) {
        Optional<Order> existingOrderOptional = orderRepository.findById(order.getOrderId());

        if (existingOrderOptional.isPresent()) {
            Order existingOrder = existingOrderOptional.get();
            processOrder(existingOrder);

            return new ResponseEntity<>(existingOrder, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private void processOrder(Order order) {
        List<Map<String, Object>> orderList = order.getOrderList();

        for (Map<String, Object> orderItem : orderList) {
            List<Map<String, Object>> items = (List<Map<String, Object>>) orderItem.get("items");

            // this is for Removing prepared items from the list
            items.removeIf(item -> Boolean.TRUE.equals(item.get("prepared")));
        }
    }



    //get all orders restaurant using restaurantId
    @PostMapping("/dinedynamo/restaurant/orders/getallorders")
    public ResponseEntity<ApiResponse> getAllOrders(@RequestBody Restaurant restaurant){


        restaurant = restaurantRepository.findById(restaurant.getRestaurantId()).orElse(null);


        if (restaurant == null)
        {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "success", null), HttpStatus.NOT_FOUND);
        }
        else
        {
            List<Order> orders = orderRepository.findByRestaurantId(restaurant.getRestaurantId());

            return new ResponseEntity<ApiResponse>(new ApiResponse(HttpStatus.OK, "success", orders), HttpStatus.OK);

        }
    }


    //delete order using particular orderId
    @DeleteMapping("/dinedynamo/restaurant/orders")
    public ResponseEntity<ApiResponse> deleteOrder(@RequestBody Order order) {
        Optional<Order> deleteOrder = orderRepository.findById(order.getOrderId());

        if (deleteOrder.isPresent()) {
            orderRepository.deleteById(order.getOrderId());
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", null), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "failure", null), HttpStatus.NOT_FOUND);
        }
    }

    //update order using orderId
    @PostMapping("/dinedynamo/restaurant/orders/updateorder")
    public ResponseEntity<ApiResponse> updateOrder( @RequestBody Order order) {
        try {
            Order existingOrder = orderRepository.findById(order.getOrderId()).orElse(null);

            if (existingOrder != null) {

                existingOrder.setOrderList(order.getOrderList());

                orderRepository.save(existingOrder);
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



