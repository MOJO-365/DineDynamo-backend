package com.dinedynamo.controllers;

import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.FinalBill;
import com.dinedynamo.collections.Restaurant;
import com.dinedynamo.collections.order_collections.Order;
import com.dinedynamo.collections.order_collections.OrderList;
import com.dinedynamo.collections.order_collections.Addon;
import com.dinedynamo.repositories.FinalBillRepository;
import com.dinedynamo.repositories.order_repositories.NewOrderRepository;
import com.dinedynamo.repositories.RestaurantRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@CrossOrigin("*")
public class NewOrderController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private NewOrderRepository orderRepository;

    @Autowired
    private FinalBillRepository finalBillRepository;

    // Place order
    @PostMapping("/dinedynamo/restaurant/orders/place")
    public ResponseEntity<ApiResponse> placeOrder(@RequestBody Order order) {
        orderRepository.save(order);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Success", order), HttpStatus.OK);
    }

    // Get all orders for a restaurant using restaurantId
    @PostMapping("/dinedynamo/restaurant/orders/all")
    public ResponseEntity<ApiResponse> getAllOrders(@RequestBody Restaurant restaurant) {
        restaurant = restaurantRepository.findById(restaurant.getRestaurantId()).orElse(null);

        if (restaurant == null) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "Restaurant not found", null), HttpStatus.NOT_FOUND);
        } else {
            List<Order> orders = orderRepository.findByRestaurantId(restaurant.getRestaurantId());
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Success", orders), HttpStatus.OK);
        }
    }

    // Delete order using orderId
    @DeleteMapping("/dinedynamo/restaurant/orders/delete")
    public ResponseEntity<ApiResponse> deleteOrder(@RequestBody Order order) {
        Optional<Order> deleteOrder = orderRepository.findById(order.getOrderId());

        if (deleteOrder.isPresent()) {
            orderRepository.deleteById(order.getOrderId());
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Order deleted successfully", null), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "Order not found", null), HttpStatus.NOT_FOUND);
        }
    }

    // Update order using orderId
    @PostMapping("/dinedynamo/restaurant/orders/update")
    public ResponseEntity<ApiResponse> updateOrder(@RequestBody Order order) {
        try {
            Order existingOrder = orderRepository.findById(order.getOrderId()).orElse(null);

            if (existingOrder != null) {
                existingOrder.setOrderList(order.getOrderList());
                orderRepository.save(existingOrder);
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Order updated successfully", null), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "Order not found", null), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/dinedynamo/restaurant/orders/process")
    public ResponseEntity<ApiResponse> processOrderDetail(@RequestBody Order order) {
        List<Order> orders = orderRepository.findByTableId(order.getTableId());

        List<Map<String, Object>> data = processData(orders);
        Map<String, Object> responseData = new HashMap<>();
        if (!orders.isEmpty()) {
            responseData.put("restaurantId", orders.get(0).getRestaurantId());
            responseData.put("tableId", orders.get(0).getTableId());
            responseData.put("dateTime", orders.get(0).getDateTime());
        }
        responseData.put("data", data);

        FinalBill finalBill = new FinalBill();
        finalBill.setRestaurantId(order.getRestaurantId());
        finalBill.setTableId(order.getTableId());
        finalBill.setDateTime(order.getDateTime());
        // Inside your controller method
        ObjectMapper objectMapper = new ObjectMapper();
        List<OrderList> orderList = new ArrayList<>();

        // Iterate over the list of maps and map each map to an OrderList object
        for (Map<String, Object> item : data) {
            OrderList orderListItem = objectMapper.convertValue(item, OrderList.class);
            orderList.add(orderListItem);

        }

        finalBill.setOrderList(orderList);
        finalBillRepository.save(finalBill);

        ApiResponse response = new ApiResponse(HttpStatus.OK, "success", responseData);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private List<Map<String, Object>> processData(List<Order> orders) {
        Map<String, Map<String, Object>> itemMap = new HashMap<>();

        for (Order order : orders) {
            for (OrderList orderList : order.getOrderList()) {
                String key = orderList.getName() + "_" + orderList.getPrice();
                if (itemMap.containsKey(key)) {
                    Map<String, Object> item = itemMap.get(key);
                    int qty = (int) item.get("qty") + orderList.getQty();
                    item.put("qty", qty);
                } else {
                    Map<String, Object> item = new HashMap<>();
                    item.put("itemId", orderList.getItemId());
                    item.put("name", orderList.getName());
                    item.put("qty", orderList.getQty());
                    item.put("price", orderList.getPrice());
                    itemMap.put(key, item);
                }


                if (orderList.getAddons() != null) {
                    for (Addon addon : orderList.getAddons()) {
                        String addonKey = addon.getName() + "_" + addon.getPrice();
                        if (itemMap.containsKey(addonKey)) {
                            Map<String, Object> addonItem = itemMap.get(addonKey);
                            int addonQty = (int) addonItem.get("qty") + addon.getQty();
                            addonItem.put("qty", addonQty);
                        } else {
                            Map<String, Object> addonItem = new HashMap<>();
                            addonItem.put("itemId", addon.getItemId());
                            addonItem.put("name", addon.getName());
                            addonItem.put("qty", addon.getQty());
                            addonItem.put("price", addon.getPrice());
                            itemMap.put(addonKey, addonItem);
                        }
                    }
                }
            }
        }

        return new ArrayList<>(itemMap.values());
    }


    @GetMapping("dinedynamo/report/percentage-sales/{restaurantId}/{itemId}")
    public ResponseEntity<Map<String, Object>> generatePercentageSalesReport(
            @PathVariable String restaurantId,
            @PathVariable String itemId) {

        List<FinalBill> orders = finalBillRepository.findByRestaurantIdAndOrderListItemId(restaurantId, itemId);

        int totalQuantity = 0;
        double totalRevenue = 0.0;
        int itemQuantity = 0;

        for (FinalBill order : orders) {
            for (OrderList orderItem : order.getOrderList()) {
                if (itemId.equals(orderItem.getItemId())) {
                    totalQuantity += orderItem.getQty();
                    totalRevenue += orderItem.getPrice() * orderItem.getQty();
                    itemQuantity++;
                }
            }
        }

        // Calculate percentage sales
        double totalRevenueAllItems = calculateTotalRevenueAllItems(restaurantId);
        double percentageSales = (totalRevenue / totalRevenueAllItems) * 100;

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("status", "OK");
        responseData.put("message", "Percentage sales report generated successfully");
        responseData.put("data", Map.of(
                "percentageSales", percentageSales,
                "dateTime", LocalDateTime.now(),
                "itemId", itemId,
                "totalQuantity", totalQuantity,
                "itemQuantity", itemQuantity,
                "totalRevenue", totalRevenue,
                "restaurantId", restaurantId
        ));

        return ResponseEntity.ok(responseData);
    }

    private double calculateTotalRevenueAllItems(String restaurantId) {
        // Fetch all orders for the restaurant and calculate total revenue
        List<FinalBill> orders = finalBillRepository.findByRestaurantId(restaurantId);
        double totalRevenue = 0.0;
        for (FinalBill order : orders) {
            for (OrderList orderItem : order.getOrderList()) {
                totalRevenue += orderItem.getPrice() * orderItem.getQty();
            }
        }
        return totalRevenue;
    }
}