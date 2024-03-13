package com.dinedynamo.services.report_services;


import com.dinedynamo.collections.invoice_collections.TakeAwayFinalBill;
import com.dinedynamo.collections.order_collections.OrderList;
import com.dinedynamo.collections.report_collections.ItemSale;
import com.dinedynamo.collections.report_collections.OrderCounts;
import com.dinedynamo.repositories.invoice_repositories.DeliveryFinalBillRepository;
import com.dinedynamo.repositories.invoice_repositories.DineInFinalBillRepository;
import com.dinedynamo.repositories.invoice_repositories.TakeAwayFinalBillRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ReportService {


    @Autowired
    private DineInFinalBillRepository dineInFinalBillRepository;
    @Autowired
    private DeliveryFinalBillRepository deliveryFinalBillRepository;

    @Autowired
    private TakeAwayFinalBillRepository takeAwayFinalBillRepository;

    public OrderCounts getTotalOrders(String restaurantId) {
        long totalDineInOrders = dineInFinalBillRepository.countByRestaurantId(restaurantId);
        long totalDeliveryOrders = deliveryFinalBillRepository.countByRestaurantId(restaurantId);
        long totalTakeAwayOrders =  takeAwayFinalBillRepository.countByRestaurantId(restaurantId);

        return new OrderCounts(totalDineInOrders,totalDeliveryOrders, totalTakeAwayOrders);


    }




    public Map<String, Object> generateDailyOverallSalesReport(TakeAwayFinalBill takeAwayFinalBill) {
        List<TakeAwayFinalBill> takeAwayOrders = takeAwayFinalBillRepository.findByRestaurantIdAndDate(takeAwayFinalBill.getRestaurantId());

        Map<String, ItemSale> itemSalesMap = new HashMap<>();
        for (TakeAwayFinalBill order : takeAwayOrders) {
            for (OrderList orderItem : order.getOrderList()) {
                String itemId = orderItem.getItemId();
                String itemName = orderItem.getItemName();
                double itemPrice = orderItem.getItemPrice();
                int quantity = orderItem.getQty();
                double totalSales = itemPrice * quantity;

                ItemSale itemSale = itemSalesMap.getOrDefault(itemId, new ItemSale(itemId, itemName, 0, itemPrice, 0));
                itemSale.setTotalQuantity(itemSale.getTotalQuantity() + quantity);
                itemSale.setTotalSales(itemSale.getTotalSales() + totalSales);
                itemSalesMap.put(itemId, itemSale);
            }
        }

        Map<String, Object> report = new HashMap<>();
        report.put("dailyTotalSales", new ArrayList<>(itemSalesMap.values()));
        return report;
    }
}
