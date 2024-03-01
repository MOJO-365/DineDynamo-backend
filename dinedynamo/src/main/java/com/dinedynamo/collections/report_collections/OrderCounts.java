package com.dinedynamo.collections.report_collections;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderCounts {
    private long totalDeliveryOrders;
    private long totalTakeAwayOrders;
}
