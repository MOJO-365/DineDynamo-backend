package com.dinedynamo.controllers.report_controllers;

import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.invoice_collections.TakeAwayFinalBill;
import com.dinedynamo.collections.order_collections.TakeAway;
import com.dinedynamo.collections.report_collections.ItemSale;
import com.dinedynamo.collections.report_collections.OrderCounts;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.dto.report_dtos.DailyOverallSalesRequest;
import com.dinedynamo.dto.report_dtos.DailySalesReport;
import com.dinedynamo.services.report_services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;

//    @PostMapping("dinedynamo/reports/highest-selling-items")
//    public ResponseEntity<List<ItemSale>> getHighestSellingItems(@RequestBody Restaurant restaurant) {
//        List<ItemSale> highestSellingItems = reportService.getHighestSellingItems(restaurant.getRestaurantId());
//        return ResponseEntity.ok(highestSellingItems);
//    }
//
//    @GetMapping("dinedynamo/reports/orders/total")
//    public ResponseEntity<ApiResponse> getTotalOrdersCount(@RequestBody Restaurant restaurant) {
//        OrderCounts orders = reportService.getTotalOrders(restaurant.getRestaurantId());
//        ApiResponse response = new ApiResponse(HttpStatus.OK, "Success", orders);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

    @PostMapping("/dinedynamo/reports/dailyOverallSales")
    public ResponseEntity<DailySalesReport> getDailyOverallSalesReport(@RequestBody DailyOverallSalesRequest request) {
        DailySalesReport dailySalesReport = reportService.generateDailyOverallSalesReport(request.getRestaurantId(), request.getDate());
        return ResponseEntity.ok(dailySalesReport);
    }

}