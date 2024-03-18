package com.dinedynamo.dto.report_dtos;

import com.dinedynamo.collections.report_collections.ItemSale;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailySalesReport {

    private List<ItemSale> itemSales;
    private double totalRevenue;

}


