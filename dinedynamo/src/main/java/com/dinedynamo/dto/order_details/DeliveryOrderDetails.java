package com.dinedynamo.dto.order_details;

import com.dinedynamo.collections.order_collections.OrderList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DeliveryOrderDetails {

        private String deliveryBillId;
        private LocalDateTime datetime;
        private List<OrderList> orderList;
        private double totalAmount;

}
