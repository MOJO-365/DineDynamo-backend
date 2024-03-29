package com.dinedynamo.collections.invoice_collections;

import com.dinedynamo.collections.order_collections.OrderList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "delivery-final-bills")
public class DeliveryFinalBill {

    @Id
    private String deliveryBillId;
    private String restaurantId;
    private LocalDateTime datetime;
    private LocalDate date;
    private String orderType;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String customerAddress;
    private double totalAmount;
    private List<OrderList> orderList;

}



//private String orderType;
