package com.dinedynamo.dto.report_dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DateRangeRequest {
    private String restaurantId;
    private LocalDate fromDate;
    private LocalDate toDate;
}
