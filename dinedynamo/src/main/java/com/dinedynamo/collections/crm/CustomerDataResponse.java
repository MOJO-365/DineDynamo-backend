package com.dinedynamo.collections.crm;

import com.dinedynamo.collections.invoice_collections.DeliveryFinalBill;
import com.dinedynamo.collections.invoice_collections.TakeAwayFinalBill;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerDataResponse {
    private List<DeliveryFinalBill> deliveryFinalBills;
    private List<TakeAwayFinalBill> takeAwayFinalBills;
}
