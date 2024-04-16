package com.dinedynamo.collections.invoice_collections;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PastOrderHistoryResponse {
    private List<PastOrderInfo> pastOrders;
}
