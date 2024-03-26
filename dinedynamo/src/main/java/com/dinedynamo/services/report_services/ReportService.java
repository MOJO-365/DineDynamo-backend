package com.dinedynamo.services.report_services;

import com.dinedynamo.collections.invoice_collections.DineInFinalBill;
import com.dinedynamo.collections.invoice_collections.DeliveryFinalBill;
import com.dinedynamo.collections.invoice_collections.TakeAwayFinalBill;
import com.dinedynamo.collections.menu_collections.Category;
import com.dinedynamo.collections.menu_collections.MenuItem;
import com.dinedynamo.collections.order_collections.OrderList;
import com.dinedynamo.collections.report_collections.ItemSale;
import com.dinedynamo.collections.report_collections.OrderCounts;
import com.dinedynamo.dto.report_dtos.*;
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
            if (order.getDate().isEqual(date)) {
                processOrder(order.getOrderList(), order.getDate(), OrderType.TAKEAWAY, itemSales);
            }
        }

        List<DineInFinalBill> dineInOrders = dineInFinalBillRepository.findByRestaurantIdAndDate(restaurantId, date);
        for (DineInFinalBill order : dineInOrders) {
            if (order.getDate().isEqual(date)) {
                processOrder(order.getOrderList(), order.getDate(), OrderType.DINEIN, itemSales);
            }
        }

        List<DeliveryFinalBill> deliveryOrders = deliveryFinalBillRepository.findByRestaurantIdAndDate(restaurantId, date);
        for (DeliveryFinalBill order : deliveryOrders) {
            if (order.getDate().isEqual(date)) {
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
                if (sale.getItemName().equals(itemName) && sale.getPrice() == itemPrice && sale.getOrderType() == orderType) {
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

        List<TakeAwayFinalBill> takeAwayOrders = takeAwayFinalBillRepository.findByRestaurantId(restaurantId);
        for (TakeAwayFinalBill order : takeAwayOrders) {
            processOrder(order.getOrderList(), order.getDate(), OrderType.TAKEAWAY, itemSales);
        }

        List<DineInFinalBill> dineInOrders = dineInFinalBillRepository.findByRestaurantId(restaurantId);
        for (DineInFinalBill order : dineInOrders) {
            processOrder(order.getOrderList(), order.getDate(), OrderType.DINEIN, itemSales);
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


    public TotalSalesReport getTotalSalesReport(String restaurantId) {
        double totalDineInSales = getTotalSalesByOrderType(restaurantId, OrderType.DINEIN);
        double totalDeliverySales = getTotalSalesByOrderType(restaurantId, OrderType.DELIVERY);
        double totalTakeAwaySales = getTotalSalesByOrderType(restaurantId, OrderType.TAKEAWAY);

        return new TotalSalesReport(totalDineInSales, totalDeliverySales, totalTakeAwaySales);
    }

    private double getTotalSalesByOrderType(String restaurantId, OrderType orderType) {
        double totalSales = 0.0;

        switch (orderType) {
            case DINEIN:
                List<DineInFinalBill> dineInOrders = dineInFinalBillRepository.findByRestaurantId(restaurantId);
                for (DineInFinalBill order : dineInOrders) {
                    totalSales += calculateTotalSales(order.getOrderList());
                }
                break;
            case DELIVERY:
                List<DeliveryFinalBill> deliveryOrders = deliveryFinalBillRepository.findByRestaurantId(restaurantId);
                for (DeliveryFinalBill order : deliveryOrders) {
                    totalSales += calculateTotalSales(order.getOrderList());
                }
                break;
            case TAKEAWAY:
                List<TakeAwayFinalBill> takeAwayOrders = takeAwayFinalBillRepository.findByRestaurantId(restaurantId);
                for (TakeAwayFinalBill order : takeAwayOrders) {
                    totalSales += calculateTotalSales(order.getOrderList());
                }
                break;
            default:
                break;
        }

        return totalSales;
    }

    private double calculateTotalSales(List<OrderList> orderList) {
        double totalSales = 0.0;
        for (OrderList orderItem : orderList) {
            totalSales += orderItem.getItemPrice() * orderItem.getQty();
        }
        return totalSales;
    }


    public LastFiveDaysSalesReport getLastFiveDaysSales(String restaurantId) {
        LocalDate today = LocalDate.now();
        LocalDate fiveDaysAgo = today.minusDays(5);

        List<DateSales> lastFiveDaysSales = new ArrayList<>();

        for (LocalDate date = today.minusDays(1); !date.isBefore(fiveDaysAgo); date = date.minusDays(1)) {
            if (!date.isEqual(today))
            {
                double totalSales = getTotalSalesForDate(restaurantId, date);
                lastFiveDaysSales.add(new DateSales(date, totalSales));
            }
        }

        return new LastFiveDaysSalesReport(lastFiveDaysSales);
    }


    private double getTotalSalesForDate(String restaurantId, LocalDate date) {
        double totalSales = 0.0;

        totalSales += getTotalSalesForDate(dineInFinalBillRepository.findByRestaurantIdAndDate(restaurantId, date));
        totalSales += getTotalSalesForDate(deliveryFinalBillRepository.findByRestaurantIdAndDate(restaurantId, date));
        totalSales += getTotalSalesForDate(takeAwayFinalBillRepository.findByRestaurantIdAndDate(restaurantId, date));

        return totalSales;
    }

    private double getTotalSalesForDate(List<? extends Object> salesReports) {
        return salesReports.stream()
                .mapToDouble(order -> {
                    if (order instanceof DineInFinalBill) {
                        return calculateTotalSales(((DineInFinalBill) order).getOrderList());
                    } else if (order instanceof DeliveryFinalBill) {
                        return calculateTotalSales(((DeliveryFinalBill) order).getOrderList());
                    } else if (order instanceof TakeAwayFinalBill) {
                        return calculateTotalSales(((TakeAwayFinalBill) order).getOrderList());
                    }
                    return 0.0;
                })
                .sum();
    }


    public List<DailySalesReport> generateReportsForDateRange(String restaurantId, LocalDate fromDate, LocalDate toDate) {
        List<DailySalesReport> reports = new ArrayList<>();

        for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {
            DailySalesReport report = generateDailyOverallSalesReport(restaurantId, date);
            reports.add(report);
        }

        List<ItemSale> mergedItemSales = new ArrayList<>();
        double totalRevenue = 0.0;
        for (DailySalesReport report : reports) {
            mergedItemSales.addAll(report.getItemSales());
            totalRevenue += report.getTotalRevenue();
        }

        DailySalesReport mergedReport = new DailySalesReport(mergedItemSales, totalRevenue);
        List<DailySalesReport> mergedReports = new ArrayList<>();
        mergedReports.add(mergedReport);

        return mergedReports;
    }


}