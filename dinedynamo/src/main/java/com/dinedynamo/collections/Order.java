package com.dinedynamo.collections;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.springframework.data.annotation.Id;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Document(collection = "orders")
public class Order {

    @Id
    private String orderId;
    private String restaurantId;
    private String tableId;
    private Double totalPrice;
    //private String orderStatus;
    private JSONArray OrderList;

}