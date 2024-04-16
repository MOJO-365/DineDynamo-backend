package com.dinedynamo.collections.invoice_collections;

import com.dinedynamo.collections.order_collections.OrderList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PastOrderInfo {
    private String customerName;
    private String customerAddress;
    private String customerPhone;
    private String orderType;
    private LocalDateTime dateTime;
    private double totalAmount;
    private String customerEmail;
    private List<OrderList> orderList;
}
