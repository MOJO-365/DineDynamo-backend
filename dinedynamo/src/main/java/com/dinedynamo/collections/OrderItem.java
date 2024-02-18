package com.dinedynamo.collections;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItem {

    private String itemName;
    private int quantity;
    private double price;
    private List<Addon> addons;
}

