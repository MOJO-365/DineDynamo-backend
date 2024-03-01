package com.dinedynamo.services.report_services;


import com.dinedynamo.collections.order_collections.OrderList;
import com.dinedynamo.collections.order_collections.TakeAway;
import com.dinedynamo.collections.report_collections.ItemSale;
import com.dinedynamo.collections.report_collections.OrderCounts;
import com.dinedynamo.repositories.order_repositories.NewDeliveryRepository;
import com.dinedynamo.repositories.order_repositories.NewTakeAwayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private NewDeliveryRepository deliveryOrderRepository;

    @Autowired
    private NewTakeAwayRepository takeAwayRepository;

    public OrderCounts getTotalOrders(String restaurantId) {
        long totalDeliveryOrders = deliveryOrderRepository.countByRestaurantId(restaurantId);
        long totalTakeAwayOrders = takeAwayRepository.countByRestaurantId(restaurantId);

        return new OrderCounts(totalDeliveryOrders, totalTakeAwayOrders);


    }


    public Map<String, Object> generateDailyOverallSalesReport(String restaurantId, LocalDateTime dateTime) {

        List<TakeAway> takeAwayOrders = takeAwayRepository.findByRestaurantIdAndDateTime(restaurantId, dateTime);

        Map<String, ItemSale> itemSalesMap = new HashMap<>();
        for (TakeAway order : takeAwayOrders) {
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
