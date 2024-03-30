package com.dinedynamo.collections.report_collections;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DateRangeRequest {
    private LocalDate fromDate;
    private LocalDate toDate;
}
