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
    private String name;
    private int qty;
    private double price;
    private String desc;
    private String note;
    private List<Addon> addons;

}
