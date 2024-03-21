package com.dinedynamo.dto.report_dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TopItem {
    private String itemName;
    private double totalSales;
    private int rank;
}