package com.dinedynamo.collections;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class OrderItem {
    private String itemName;
    private String description;
    private int quantity;
    private double price;

}
