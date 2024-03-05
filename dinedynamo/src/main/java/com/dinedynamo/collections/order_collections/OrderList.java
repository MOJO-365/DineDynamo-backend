package com.dinedynamo.collections.order_collections;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderList {

    private String itemId;
    private String itemName;
    private int qty;
    private double itemPrice;
    private String itemDescription;
    private String note;
    private boolean prepared;


}



//    private List<Addon> addons;
