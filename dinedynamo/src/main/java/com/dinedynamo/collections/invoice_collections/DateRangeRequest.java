package com.dinedynamo.collections.invoice_collections;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DateRangeRequest {
    private LocalDate fromDate;
    private LocalDate toDate;
}
