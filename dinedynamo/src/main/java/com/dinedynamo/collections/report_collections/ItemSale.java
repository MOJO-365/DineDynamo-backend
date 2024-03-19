package com.dinedynamo.collections.report_collections;

import com.dinedynamo.dto.report_dtos.OrderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemSale {
    private String itemId;
    private LocalDate date;
    private String itemName;
    private String categoryName;
    private int totalQuantity;
    private double price;
    private double totalSales;
    private OrderType orderType;
}
