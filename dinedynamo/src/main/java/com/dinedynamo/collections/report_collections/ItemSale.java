package com.dinedynamo.collections.report_collections;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemSale {


    private String itemId;
    private String itemName;
    private int totalQuantity;
    private double price;
    private double totalSales;
}