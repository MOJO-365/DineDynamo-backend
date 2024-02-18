package com.dinedynamo.collections;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.data.annotation.Id;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

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
    private String dateTime;
    private double grandTotal;
    private JSONArray OrderList;

}