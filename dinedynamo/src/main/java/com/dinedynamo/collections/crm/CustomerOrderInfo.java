package com.dinedynamo.collections.crm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerOrderInfo {
    private String customerName;
    private String customerAddress;
    private String customerPhone;
    private String orderType;
    private LocalDateTime date;
    private double totalAmount;
    private String customerEmail;
}