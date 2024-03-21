package com.dinedynamo.dto.report_dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TotalSalesReport {

    private double totalDineInSales;
    private double totalDeliverySales;
    private double totalTakeAwaySales;

}