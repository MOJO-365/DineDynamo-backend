package com.dinedynamo.collections.order_collections;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "delivery-orders")
public class DeliveryOrder {

        @Id
        private String deliveryId;
        private String restaurantId;
        private String customerEmail;
        private String  customerName;
        private String  customerPhone;
        private String  customerAddress;
        private String  dateTime;
        private boolean deliveryStatus;
        private List<OrderList> orderList;


}
