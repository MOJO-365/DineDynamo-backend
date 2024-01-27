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
import java.util.Optional;



@RestController
@CrossOrigin("*")
//@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TableRepository tableRepository;


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

 //restaurant orders
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
            return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failure", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}



