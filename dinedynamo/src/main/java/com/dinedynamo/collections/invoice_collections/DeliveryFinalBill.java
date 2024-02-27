package com.dinedynamo.collections.invoice_collections;

import com.dinedynamo.collections.order_collections.OrderList;
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
@Document(collection = "delivery_final_bills")
public class DeliveryFinalBill {

    @Id
    private String deliveryBillId;
    private String restaurantId;
    private String datetime;
    private List<OrderList> orderList;
    private String orderType;
    private double gst;
    private double totalAmount;

}
