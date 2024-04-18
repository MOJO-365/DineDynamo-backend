package com.dinedynamo.controllers.invoice_controllers;

import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.invoice_collections.DeliveryFinalBill;
import com.dinedynamo.collections.invoice_collections.DineInFinalBill;
import com.dinedynamo.dto.order_details.DeliveryOrderDetails;
import com.dinedynamo.repositories.order_repositories.DineInOrderRepository;
import com.dinedynamo.services.external_services.SmsService;
import com.dinedynamo.services.invoice_services.DineInBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
public class DineInBillController {

    @Autowired
    private  DineInBillService dineInBillService;

    @Autowired
    private DineInOrderRepository dineInOrderRepository;

    @Autowired
    private SmsService smsService;

    @PostMapping("/dinedynamo/restaurant/orders/save/dinein-final-bill")
    public ResponseEntity<ApiResponse> saveDineInBill(@RequestBody DineInFinalBill dineInFinalBill) {
        dineInFinalBill.setDatetime(LocalDateTime.now());
        dineInFinalBill.setDate(LocalDate.now());
        dineInFinalBill.setOrderType("DineIn");
        dineInBillService.saveDineInBill(dineInFinalBill);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Data Stored", dineInFinalBill), HttpStatus.OK);
    }

}
