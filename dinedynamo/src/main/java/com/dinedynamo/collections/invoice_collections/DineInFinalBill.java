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
import java.util.Date;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "dinein-final-bills")
public class DineInFinalBill {

    @Id
    private String dineInBillId;
    private String restaurantId;
    private String tableId;
    private String orderType;
    private String customerName;
    private String customerPhone;
    private LocalDate date;
    private LocalDateTime datetime;
    private double totalAmount;
    private List<OrderList> orderList;

}

//private String paymentMode;

