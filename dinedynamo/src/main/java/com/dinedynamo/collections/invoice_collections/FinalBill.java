package com.dinedynamo.collections.invoice_collections;

import com.dinedynamo.collections.order_collections.OrderList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "final-bills")
public class FinalBill {

    @Id
    private String billId;
    private String restaurantId;
    private String tableId;
    private String dateTime;
    private String orderType;
    private String paymentMode;
    private double gst;
    private double totalAmount;
    private List<OrderList> orderList;

}
