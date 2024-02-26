package com.dinedynamo.collections.old_order_collections;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Document(collection = "delivery")
public class DeliveryOrder {

    @Id
    private String  deliveryId;
    private String  restaurantId;
    private String  customerName;
    private String  customerPhone;
    private String  customerAddress;
    private String  dateTime;
    private JSONArray OrderList;


}