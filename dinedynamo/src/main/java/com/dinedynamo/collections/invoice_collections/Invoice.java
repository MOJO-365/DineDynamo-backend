package com.dinedynamo.collections.invoice_collections;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Document(collection = "invoices")
public class Invoice {
    @Id
    private String invoiceId;
    private String customerId;
    private String orderId;
    private String tableId;
    private Double totalPrice;
    private Double gst;
    private byte[] pdfData;


}


