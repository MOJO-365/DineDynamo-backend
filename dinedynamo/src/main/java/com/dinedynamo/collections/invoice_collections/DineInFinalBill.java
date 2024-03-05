package com.dinedynamo.collections.invoice_collections;

import com.dinedynamo.collections.order_collections.OrderList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "dinein_final_bills")
public class DineInFinalBill {

    @Id
    private String dineInBillId;
    private String restaurantId;
    private String tableId;
    private LocalDateTime datetime;
    private double gst;
    private double totalAmount;
    private List<OrderList> orderList;

}

//private String orderType;
//private String paymentMode;

