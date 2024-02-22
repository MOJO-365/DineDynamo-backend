package com.dinedynamo.controllers;

import com.dinedynamo.collections.report_collection.ItemSale;
import com.dinedynamo.collections.Restaurant;
import com.dinedynamo.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("dinedynamo/reports/highest-selling-items")
    public ResponseEntity<List<ItemSale>> getHighestSellingItems(@RequestBody Restaurant restaurant) {
        List<ItemSale> highestSellingItems = reportService.getHighestSellingItems(restaurant.getRestaurantId());
        return ResponseEntity.ok(highestSellingItems);
    }
}
