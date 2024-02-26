package com.dinedynamo.controllers.old_orders_controllers;

import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.authentication_collections.Restaurant;
import com.dinedynamo.collections.old_order_collections.Order;
import com.dinedynamo.repositories.OrderRepository;
import com.dinedynamo.repositories.RestaurantRepository;
import com.dinedynamo.repositories.TableRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dinedynamo.repositories.CustomerRepository;

import java.util.HashMap;
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
        orderRepository.save(order);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Success", order), HttpStatus.OK);
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

            Order existingOrder = orderRepository.findById(order.getOrderId()).orElse(null);

            if (existingOrder != null) {

                existingOrder.setOrderList(order.getOrderList());

                orderRepository.save(existingOrder);
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", null), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "failure", null), HttpStatus.NOT_FOUND);
            }

    }





    @PostMapping("/dinedynamo/invoice/get-order-items")
    public ResponseEntity<ApiResponse> getFinalOrderForTable(@RequestBody Order order) {

            if (order.getTableId() == null) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "Table ID cannot be null", null), HttpStatus.BAD_REQUEST);
            }

            List<Order> orderListForTable = orderRepository.findByTableId(order.getTableId());

            if (!orderListForTable.isEmpty()) {
                Order consolidatedOrder = consolidateOrders(orderListForTable);
                consolidatedOrder.setTableId(order.getTableId());

                Order firstOrder = orderListForTable.get(0);
                consolidatedOrder.setRestaurantId(firstOrder.getRestaurantId());
                consolidatedOrder.setOrderId(firstOrder.getOrderId());
                consolidatedOrder.setDateTime(firstOrder.getDateTime());

                ApiResponse response = new ApiResponse(HttpStatus.OK, "Orders retrieved successfully", List.of(consolidatedOrder));
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "No orders found for the specified table", null), HttpStatus.NOT_FOUND);
            }

    }


    private Order consolidateOrders(List<Order> orderList) {
        if (orderList.isEmpty()) {
            return null;
        }

        Order consolidatedOrder = new Order();
        Map<String, Integer> itemNameToTotalQuantity = new HashMap<>();
        Map<String, Double> itemNameToPrice = new HashMap<>();
        Map<String, List<Map<String, Object>>> itemNameToAddons = new HashMap<>();

        for (Order order : orderList) {
            List<Map<String, Object>> orderListArray = order.getOrderList();
            for (Map<String, Object> item : orderListArray){

                String itemName = (String) item.get("name");
                double price = ((Number) item.get("price")).doubleValue();
                int qty = ((Number) item.get("qty")).intValue();

                String key = itemName + "_" + price;
                itemNameToTotalQuantity.put(key, itemNameToTotalQuantity.getOrDefault(key, 0) + qty);
                itemNameToPrice.put(key, price);

                List<Map<String, Object>> addons = (List<Map<String, Object>>) item.get("addons");
                if (addons != null && !addons.isEmpty()) {
                    for (Map<String, Object> addon : addons) {
                        String addonName = (String) addon.get("name");
                        double addonPrice = ((Number) addon.get("price")).doubleValue();
                        int addonQty = ((Number) addon.get("qty")).intValue();

                        // Update addon quantity
                        String addonKey = addonName + "_" + addonPrice;
                        itemNameToTotalQuantity.put(addonKey, itemNameToTotalQuantity.getOrDefault(addonKey, 0) + addonQty);
                        itemNameToPrice.put(addonKey, addonPrice);
                    }
                }
            }
        }

        JSONArray combinedOrderList = new JSONArray();
        for (String key : itemNameToTotalQuantity.keySet()) {
            String[] parts = key.split("_");
            String itemName = parts[0];
            double price = Double.parseDouble(parts[1]);
            int totalQuantity = itemNameToTotalQuantity.get(key);

            JSONObject combinedItem = new JSONObject();
            combinedItem.put("name", itemName);
            combinedItem.put("price", price);
            combinedItem.put("qty", totalQuantity);

            combinedOrderList.add(combinedItem);
        }

        consolidatedOrder.setOrderList(combinedOrderList);
        return consolidatedOrder;
    }




}


