package com.dinedynamo.collections.report_collections;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderListTotal {
    private long totalDineInItems;
    private long totalDeliveryItems;
    private long totalTakeAwayItems;
}