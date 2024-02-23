package com.dinedynamo.services;

import com.dinedynamo.collections.FinalBill;
import com.dinedynamo.collections.order_collections.OrderList;
import com.dinedynamo.collections.report_collection.ItemSale;
import com.dinedynamo.repositories.FinalBillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReportService {

    @Autowired
    private FinalBillRepository finalBillRepository;

    public List<ItemSale> getHighestSellingItems(String restaurantId) {
        List<FinalBill> finalBills = finalBillRepository.findByRestaurantId(restaurantId);
        Map<String, Integer> totalQuantities = new HashMap<>();
        Map<String, Double> totalSales = new HashMap<>();

        for (FinalBill finalBill : finalBills) {
            List<OrderList> orderList = finalBill.getOrderList();
            if (orderList != null) {
                for (OrderList order : orderList) {
                    String itemId = order.getItemId();
                    int quantity = order.getQty();
                    double price = order.getPrice();
                    totalQuantities.put(itemId, totalQuantities.getOrDefault(itemId, 0) + quantity);
                    totalSales.put(itemId, totalSales.getOrDefault(itemId, 0.0) + (quantity * price));
                }
            }
        }

        List<ItemSale> itemSales = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : totalQuantities.entrySet()) {
            String itemId = entry.getKey();
            int totalQuantity = entry.getValue();
            double totalItemSales = totalSales.get(itemId);
            String itemName = "";
            Optional<String> optionalName = finalBills.stream()
                    .flatMap(finalBill -> finalBill.getOrderList().stream())
                    .filter(orderList -> orderList.getItemId().equals(itemId))
                    .map(OrderList::getName)
                    .findFirst();
            if (optionalName.isPresent()) {
                itemName = optionalName.get();
            }
            double itemPrice = 0.0;
            Optional<Double> optionalPrice = finalBills.stream()
                    .flatMap(finalBill -> finalBill.getOrderList().stream())
                    .filter(orderList -> orderList.getItemId().equals(itemId))
                    .map(OrderList::getPrice)
                    .findFirst();
            if (optionalPrice.isPresent()) {
                itemPrice = optionalPrice.get();
            }
            ItemSale itemSale = new ItemSale(itemId, itemName, totalQuantity, itemPrice, totalItemSales);
            itemSales.add(itemSale);
        }

        itemSales.sort(Comparator.comparingDouble(ItemSale::getTotalSales).reversed());

        return itemSales;
    }
}
