package com.dinedynamo.controllers.invoice_controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.invoice_collections.TakeAwayFinalBill;
import com.dinedynamo.dto.order_details.TakeAwayOrderDetails;
import com.dinedynamo.services.invoice_services.TakeAwayBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
public class TakeAwayBillController {

    @Autowired
    private TakeAwayBillService takeAwayBillService;

    @PostMapping("/dinedynamo/restaurant/orders/save/takeaway-final-bill")
    public ResponseEntity<ApiResponse> saveTakeAwayBill(@RequestBody TakeAwayFinalBill takeAwayFinalBill){
        takeAwayFinalBill.setDatetime(LocalDateTime.now());
        takeAwayFinalBill.setDate( LocalDate.now());
        takeAwayFinalBill.setOrderType("TakeAway");
        takeAwayBillService.saveTakeAwayBill(takeAwayFinalBill);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"Data Stored",takeAwayFinalBill),HttpStatus.OK);
    }

    @PostMapping("/dinedynamo/customer/take-away-orders")
    public ResponseEntity<ApiResponse> getCustomerOrders(@RequestParam String customerPhone) {
        List<TakeAwayFinalBill> customerOrders = takeAwayBillService.getCustomerOrdersByPhone(customerPhone);

        if (customerOrders.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "No orders found for this customer", null));
        }

        List<TakeAwayOrderDetails> basicOrderDetails = extractOrderDetails(customerOrders);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "Data Retrieved", basicOrderDetails));
    }
    private List<TakeAwayOrderDetails> extractOrderDetails(List<TakeAwayFinalBill> orders) {
        List<TakeAwayOrderDetails> takeAwayOrderDetails = new ArrayList<>();
        for (TakeAwayFinalBill order : orders) {
            TakeAwayOrderDetails basicOrder = new TakeAwayOrderDetails(
                    order.getTakeAwayBillId(),
                    order.getDatetime(),
                    order.getOrderList(),
                    order.getTotalAmount()
            );
            takeAwayOrderDetails.add(basicOrder);
        }
        return takeAwayOrderDetails;
    }

}
