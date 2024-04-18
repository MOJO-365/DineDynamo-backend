package com.dinedynamo.controllers.invoice_controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.invoice_collections.DeliveryFinalBill;
import com.dinedynamo.collections.invoice_collections.DineInFinalBill;
import com.dinedynamo.collections.invoice_collections.TakeAwayFinalBill;
import com.dinedynamo.dto.order_details.DeliveryOrderDetails;
import com.dinedynamo.dto.order_details.TakeAwayOrderDetails;
import com.dinedynamo.services.invoice_services.TakeAwayBillService;
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



}
