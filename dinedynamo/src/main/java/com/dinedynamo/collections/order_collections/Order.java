package com.dinedynamo.collections.order_collections;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Document(collection = "dineIn_orders")
public class Order
{
    @Id
    private String orderId;
    private String restaurantId;
    private String tableId;
    private LocalDateTime dateTime;
    private boolean prepared;
    private List<OrderList> orderList;

}