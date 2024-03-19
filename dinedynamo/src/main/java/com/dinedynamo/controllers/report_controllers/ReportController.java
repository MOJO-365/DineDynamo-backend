package com.dinedynamo.controllers.report_controllers;

import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.report_collections.OrderCounts;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.dto.report_dtos.DailyOverallSalesRequest;
import com.dinedynamo.dto.report_dtos.DailySalesReport;
import com.dinedynamo.dto.report_dtos.TopItem;
import com.dinedynamo.dto.report_dtos.TotalSalesReport;
import com.dinedynamo.services.report_services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin("*")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/dinedynamo/reports/orders/total")
    public ResponseEntity<ApiResponse> getTotalOrdersCount(@RequestBody Restaurant restaurant) {
    OrderCounts orders = reportService.getTotalOrders(restaurant.getRestaurantId());

    if (orders != null) {
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", orders), HttpStatus.OK);
    } else {
        return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "No orders found for the given restaurant", "failure"), HttpStatus.NOT_FOUND);
    }
   }


    @PostMapping("/dinedynamo/reports/dailyOverallSales")
    public ResponseEntity<ApiResponse> getDailyOverallSalesReport(@RequestBody DailyOverallSalesRequest request) {
        DailySalesReport dailySalesReport = reportService.generateDailyOverallSalesReport(request.getRestaurantId(),request.getDate());

        if (dailySalesReport != null && !dailySalesReport.getItemSales().isEmpty()) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", dailySalesReport), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "failure", null), HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/dinedynamo/report/top-five-popular-items")
    public ResponseEntity<ApiResponse> getTopFiveItems(@RequestBody Restaurant restaurant) {
        List<TopItem> topItems = reportService.getTopFiveItems(restaurant.getRestaurantId());

        if (topItems != null && !topItems.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", topItems), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "failure", null), HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/dinedynamo/report/all-orders-total-sales")
    public ResponseEntity<ApiResponse> getTotalSalesReport(@RequestBody Restaurant restaurant) {
        TotalSalesReport totalSalesReport = reportService.getTotalSalesReport(restaurant.getRestaurantId());

        if (totalSalesReport != null) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", totalSalesReport), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "No sales data found", "failure"), HttpStatus.NOT_FOUND);
        }
    }
}