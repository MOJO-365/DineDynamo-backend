package com.dinedynamo.controllers.new_orders_controllers;

import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.invoice_collections.DineInFinalBill;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.collections.order_collections.Order;
import com.dinedynamo.collections.order_collections.OrderList;
import com.dinedynamo.repositories.invoice_repositories.DineInBillRepository;
import com.dinedynamo.repositories.order_repositories.NewOrderRepository;
import com.dinedynamo.repositories.restaurant_repositories.RestaurantRepository;
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
    private DineInBillRepository dineInBillRepository;

    // Place order
    @PostMapping("dinedynamo/restaurant/orders/place")
    public ResponseEntity<ApiResponse> placeOrder(@RequestBody Order order) {
        order.setDateTime(LocalDateTime.now());
        orderRepository.save(order);
        ApiResponse response = new ApiResponse(HttpStatus.OK, "Success", order);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //get all orders restaurant using restaurantId
    @PostMapping("/dinedynamo/restaurant/orders/all")
    public ResponseEntity<ApiResponse> getAllOrders(@RequestBody Restaurant restaurant){


        restaurant = restaurantRepository.findById(restaurant.getRestaurantId()).orElse(null);


        if (restaurant == null)
        {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "success", null), HttpStatus.NOT_FOUND);
        }
        else
        {
            List<Order> orders = orderRepository.findByRestaurantId(restaurant.getRestaurantId());

            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", orders), HttpStatus.OK);

        }
    }


    // Delete order using orderId
    @DeleteMapping("dinedynamo/restaurant/orders/delete")
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
    @PostMapping("/dinedynamo/restaurant/dinein/update")
    public ResponseEntity<ApiResponse> updateOrder(@RequestBody Order updatedOrder) {
        Optional<Order> existingOrderOptional = orderRepository.findById(updatedOrder.getOrderId());

        if (existingOrderOptional.isPresent()) {
            Order existingOrder = existingOrderOptional.get();
            existingOrder.setPrepared(updatedOrder.isPrepared());
            existingOrder.setOrderList(updatedOrder.getOrderList());

            orderRepository.save(existingOrder);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "Order updated successfully", null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(HttpStatus.NOT_FOUND, "Order not found", null));
        }
    }

    @PostMapping("dinedynamo/restaurant/dine-in-orders/final-bill")
    public ResponseEntity<ApiResponse> processOrderDetail(@RequestBody Order order) {
        List<Order> orders = orderRepository.findByTableId(order.getTableId());

        if (orders.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "No orders found for the provided tableId", null), HttpStatus.NOT_FOUND);
        }

        Order firstOrder = orders.get(0);

        List<OrderList> orderList = processOrderList(orders);

        DineInFinalBill dineInFinalBill = new DineInFinalBill();
        dineInFinalBill.setRestaurantId(firstOrder.getRestaurantId());
        dineInFinalBill.setOrderList(orderList);


        ApiResponse response = new ApiResponse(HttpStatus.OK, "success", dineInFinalBill);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private List<OrderList> processOrderList(List<Order> orders) {
        Map<String, OrderList> orderListMap = new HashMap<>();

        for (Order order : orders) {
            for (OrderList orderListItem : order.getOrderList()) {
                String key = orderListItem.getItemName() + "_" + orderListItem.getItemPrice();
                if (orderListMap.containsKey(key)) {
                    OrderList existingOrderListItem = orderListMap.get(key);
                    existingOrderListItem.setQty(existingOrderListItem.getQty() + orderListItem.getQty());
                } else {
                    OrderList newOrderListItem = new OrderList();
                    newOrderListItem.setItemId(orderListItem.getItemId());
                    newOrderListItem.setItemName(orderListItem.getItemName());
                    newOrderListItem.setQty(orderListItem.getQty());
                    newOrderListItem.setItemPrice(orderListItem.getItemPrice());
                    orderListMap.put(key, newOrderListItem);
                }
            }
        }

        return new ArrayList<>(orderListMap.values());
    }









//    private List<Map<String, Object>> processData(List<Order> orders) {
//        Map<String, Map<String, Object>> itemMap = new HashMap<>();
//
//        for (Order order : orders) {
//            for (OrderList orderList : order.getOrderList()) {
//                String key = orderList.getName() + "_" + orderList.getPrice();
//                if (itemMap.containsKey(key)) {
//                    Map<String, Object> item = itemMap.get(key);
//                    int qty = (int) item.get("qty") + orderList.getQty();
//                    item.put("qty", qty);
//                } else {
//                    Map<String, Object> item = new HashMap<>();
//                    item.put("itemId", orderList.getItemId());
//                    item.put("name", orderList.getName());
//                    item.put("qty", orderList.getQty());
//                    item.put("price", orderList.getPrice());
//                    itemMap.put(key, item);
//                }
//
//
//                if (orderList.getAddons() != null) {
//                    for (Addon addon : orderList.getAddons()) {
//                        String addonKey = addon.getName() + "_" + addon.getPrice();
//                        if (itemMap.containsKey(addonKey)) {
//                            Map<String, Object> addonItem = itemMap.get(addonKey);
//                            int addonQty = (int) addonItem.get("qty") + addon.getQty();
//                            addonItem.put("qty", addonQty);
//                        } else {
//                            Map<String, Object> addonItem = new HashMap<>();
//                            addonItem.put("itemId", addon.getItemId());
//                            addonItem.put("name", addon.getName());
//                            addonItem.put("qty", addon.getQty());
//                            addonItem.put("price", addon.getPrice());
//                            itemMap.put(addonKey, addonItem);
//                        }
//                    }
//                }
//            }
//        }
//
//        return new ArrayList<>(itemMap.values());
//    }
//


//
//    @PostMapping("/dinedynamo/restaurant/orders/summary")
//    public ResponseEntity<ApiResponse> getOrderSummary(@RequestBody Restaurant restaurant) {
//        List<DineInFinalBill> orders = dineInBillRepository.findByRestaurantId(restaurant.getRestaurantId());
//
//        if (orders.isEmpty()) {
//            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "No orders found for the provided restaurantId", null), HttpStatus.NOT_FOUND);
//        }
//
//        List<Map<String, Object>> summaryList = new ArrayList<>();
//        for (DineInFinalBill order : orders) {
//            Map<String, Object> summary = new HashMap<>();
//            summary.put("orderId", order.getDineInBillId());
//            summary.put("orderDate", order.getDatetime());
//            summary.put("paymentMode", order.getPaymentMode());
//            summary.put("gst", order.getGst());
//            summary.put("totalAmount", order.getTotalAmount());
//            summary.put("orderList",order.getOrderList());
//            summaryList.add(summary);
//        }
//
//        ApiResponse response = new ApiResponse(HttpStatus.OK, "Success", summaryList);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }




//    @PostMapping("dinedynamo/report/percentage-sales/{restaurantId}/{itemId}")
//    public ResponseEntity<Map<String, Object>> generatePercentageSalesReport(
//            @PathVariable String restaurantId,
//            @PathVariable String itemId) {
//
//        List<DineInFinalBill> orders = dineInBillRepository.findByRestaurantIdAndOrderListItemId(restaurantId, itemId);
//
//        int totalQuantity = 0;
//        double totalRevenue = 0.0;
//        int itemQuantity = 0;
//
//        for (DineInFinalBill order : orders) {
//            for (OrderList orderItem : order.getOrderList()) {
//                if (itemId.equals(orderItem.getItemId())) {
//                    totalQuantity += orderItem.getQty();
//                    totalRevenue += orderItem.getItemPrice() * orderItem.getQty();
//                    itemQuantity++;
//                }
//            }
//        }
//
//        double totalRevenueAllItems = calculateTotalRevenueAllItems(restaurantId);
//        double percentageSales = (totalRevenue / totalRevenueAllItems) * 100;
//
//        Map<String, Object> responseData = new HashMap<>();
//        responseData.put("status", "OK");
//        responseData.put("message", "Percentage sales report generated successfully");
//        responseData.put("data", Map.of(
//                "percentageSales", percentageSales,
//                "dateTime", LocalDateTime.now(),
//                "itemId", itemId,
//                "totalQuantity", totalQuantity,
//                "itemQuantity", itemQuantity,
//                "totalRevenue", totalRevenue,
//                "restaurantId", restaurantId
//        ));
//
//        return ResponseEntity.ok(responseData);
//    }
//
//
//    private double calculateTotalRevenueAllItems(String restaurantId) {
//        List<DineInFinalBill> orders = dineInBillRepository.findByRestaurantId(restaurantId);
//        double totalRevenue = 0.0;
//        for (DineInFinalBill order : orders) {
//            for (OrderList orderItem : order.getOrderList()) {
//                totalRevenue += orderItem.getItemPrice() * orderItem.getQty();
//            }
//        }
//        return totalRevenue;
//    }
}