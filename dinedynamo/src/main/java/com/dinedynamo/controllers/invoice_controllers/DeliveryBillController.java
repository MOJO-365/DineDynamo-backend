package com.dinedynamo.controllers.invoice_controllers;

import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.invoice_collections.DeliveryFinalBill;
import com.dinedynamo.dto.order_details.DeliveryOrderDetails;
import com.dinedynamo.services.invoice_services.DeliveryBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
public class DeliveryBillController {
    @Autowired
    private  DeliveryBillService deliveryBillService;

    @PostMapping("/dinedynamo/restaurant/orders/save/delivery-final-bill")
    public ResponseEntity<ApiResponse> saveDeliveryBill(@RequestBody DeliveryFinalBill deliveryFinalBill) {
        deliveryFinalBill.setDatetime(LocalDateTime.now());
        deliveryFinalBill.setDate(LocalDate.now());
        deliveryFinalBill.setOrderType("Delivery");
        deliveryBillService.saveDeliveryBill(deliveryFinalBill);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Data Stored", deliveryFinalBill), HttpStatus.OK);
    }






    @PostMapping("/dinedynamo/customer/delivery-orders")
    public ResponseEntity<ApiResponse> getCustomerOrders(@RequestBody DeliveryFinalBill deliveryFinalBill) {
        String customerPhone = deliveryFinalBill.getCustomerPhone();

        List<DeliveryFinalBill> customerOrders = deliveryBillService.getCustomerOrdersByPhone(customerPhone);

        if (customerOrders.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "No orders found for this customer", null));
        }

        Collections.sort(customerOrders, (o1, o2) -> o2.getDatetime().compareTo(o1.getDatetime()));

        List<DeliveryOrderDetails> orderedOrderDetails = customerOrders.stream()
                .map(order -> new DeliveryOrderDetails(
                        order.getDeliveryBillId(),
                        order.getDatetime(),
                        order.getOrderList(),
                        order.getTotalAmount()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "Data Retrieved", orderedOrderDetails));
    }

}
