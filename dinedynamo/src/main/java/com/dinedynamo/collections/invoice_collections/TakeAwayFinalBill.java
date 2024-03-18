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
@Document(collection = "takeaway-final-bills")
public class TakeAwayFinalBill {

    @Id
    private String takeAwayBillId;
    private String restaurantId;
    private LocalDate date;
    private LocalDateTime datetime;
    private String customerPhone;
    private boolean pickedUp;
    private double totalAmount;
    private List<OrderList> orderList;


}


//private String paymentMode;
//private String orderType;
