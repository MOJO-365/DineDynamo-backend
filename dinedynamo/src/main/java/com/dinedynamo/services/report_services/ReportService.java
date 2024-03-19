package com.dinedynamo.services.report_services;

import com.dinedynamo.collections.invoice_collections.DineInFinalBill;
import com.dinedynamo.collections.invoice_collections.DeliveryFinalBill;
import com.dinedynamo.collections.invoice_collections.TakeAwayFinalBill;
import com.dinedynamo.collections.menu_collections.Category;
import com.dinedynamo.collections.menu_collections.MenuItem;
import com.dinedynamo.collections.order_collections.OrderList;
import com.dinedynamo.collections.report_collections.ItemSale;
import com.dinedynamo.collections.report_collections.OrderCounts;
import com.dinedynamo.dto.report_dtos.DailySalesReport;
import com.dinedynamo.dto.report_dtos.OrderType;
import com.dinedynamo.dto.report_dtos.TopItem;
import com.dinedynamo.repositories.invoice_repositories.DineInFinalBillRepository;
import com.dinedynamo.repositories.invoice_repositories.DeliveryFinalBillRepository;
import com.dinedynamo.repositories.invoice_repositories.TakeAwayFinalBillRepository;
import com.dinedynamo.repositories.menu_repositories.CategoryRepository;
import com.dinedynamo.repositories.menu_repositories.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private DineInFinalBillRepository dineInFinalBillRepository;

    @Autowired
    private DeliveryFinalBillRepository deliveryFinalBillRepository;

    @Autowired
    private TakeAwayFinalBillRepository takeAwayFinalBillRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    public OrderCounts getTotalOrders(String restaurantId) {
        long totalDineInOrders = dineInFinalBillRepository.countByRestaurantId(restaurantId);
        long totalDeliveryOrders = deliveryFinalBillRepository.countByRestaurantId(restaurantId);
        long totalTakeAwayOrders = takeAwayFinalBillRepository.countByRestaurantId(restaurantId);

        return new OrderCounts(totalDineInOrders, totalDeliveryOrders, totalTakeAwayOrders);
    }

    public DailySalesReport generateDailyOverallSalesReport(String restaurantId, LocalDate date) {
        List<ItemSale> itemSales = new ArrayList<>();
        double totalRevenue = 0.0;

        List<TakeAwayFinalBill> takeAwayOrders = takeAwayFinalBillRepository.findByRestaurantIdAndDate(restaurantId, date);
        for (TakeAwayFinalBill order : takeAwayOrders) {
            if (order.getDate().isEqual(date)) { // Check if dates match
                processOrder(order.getOrderList(), order.getDate(), OrderType.TAKEAWAY, itemSales);
            }
        }

        List<DineInFinalBill> dineInOrders = dineInFinalBillRepository.findByRestaurantIdAndDate(restaurantId, date);
        for (DineInFinalBill order : dineInOrders) {
            if (order.getDate().isEqual(date)) { // Check if dates match
                processOrder(order.getOrderList(), order.getDate(), OrderType.DINE_IN, itemSales);
            }
        }

        List<DeliveryFinalBill> deliveryOrders = deliveryFinalBillRepository.findByRestaurantIdAndDate(restaurantId, date);
        for (DeliveryFinalBill order : deliveryOrders) {
            if (order.getDate().isEqual(date)) { // Check if dates match
                processOrder(order.getOrderList(), order.getDate(), OrderType.DELIVERY, itemSales);
            }
        }

        for (ItemSale sale : itemSales) {
            totalRevenue += sale.getTotalSales();
        }

        return new DailySalesReport(itemSales, totalRevenue);
    }

    private void processOrder(List<OrderList> orderList, LocalDate orderDate, OrderType orderType, List<ItemSale> itemSales) {
        for (OrderList orderItem : orderList) {
            String itemId = orderItem.getItemId();
            String itemName = orderItem.getItemName();
            double itemPrice = orderItem.getItemPrice();
            int quantity = orderItem.getQty();
            double totalSales = itemPrice * quantity;

            boolean itemExists = false;
            for (ItemSale sale : itemSales) {
                if (sale.getItemName().equals(itemName) && sale.getPrice() == itemPrice) {
                    sale.setTotalQuantity(sale.getTotalQuantity() + quantity);
                    sale.setTotalSales(sale.getTotalSales() + totalSales);
                    itemExists = true;
                    break;
                }
            }

            if (!itemExists) {
                MenuItem menuItem = menuItemRepository.findById(itemId).orElse(null);
                if (menuItem != null) {
                    String parentId = menuItem.getParentId();
                    Category category = categoryRepository.findById(parentId).orElse(null);
                    if (category != null) {
                        String categoryName = category.getCategoryName();
                        ItemSale itemSale = new ItemSale(itemId, orderDate, itemName, categoryName, quantity, itemPrice, totalSales, orderType);
                        itemSales.add(itemSale);
                    }
                }
            }
        }
    }


    public List<TopItem> getTopFiveItems(String restaurantId) {
        List<ItemSale> itemSales = new ArrayList<>();

        // Fetch TakeAway orders
        List<TakeAwayFinalBill> takeAwayOrders = takeAwayFinalBillRepository.findByRestaurantId(restaurantId);
        for (TakeAwayFinalBill order : takeAwayOrders) {
            processOrder(order.getOrderList(), order.getDate(), OrderType.TAKEAWAY, itemSales);
        }

        // Fetch DineIn orders
        List<DineInFinalBill> dineInOrders = dineInFinalBillRepository.findByRestaurantId(restaurantId);
        for (DineInFinalBill order : dineInOrders) {
            processOrder(order.getOrderList(), order.getDate(), OrderType.DINE_IN, itemSales);
        }

        List<DeliveryFinalBill> deliveryOrders = deliveryFinalBillRepository.findByRestaurantId(restaurantId);
        for (DeliveryFinalBill order : deliveryOrders) {
            processOrder(order.getOrderList(), order.getDate(), OrderType.DELIVERY, itemSales);
        }

        List<TopItem> topItems = itemSales.stream()
                .collect(Collectors.groupingBy(ItemSale::getItemName,
                        Collectors.summingDouble(ItemSale::getTotalSales)))
                .entrySet().stream()
                .map(entry -> new TopItem(entry.getKey(), entry.getValue(), 0))
                .collect(Collectors.toList());

        topItems.sort(Comparator.comparingDouble(TopItem::getTotalSales).reversed());

        for (int i = 0; i < topItems.size() && i < 5; i++) {
            topItems.get(i).setRank(i + 1);
        }

        return topItems.stream().limit(5).collect(Collectors.toList());
    }








}