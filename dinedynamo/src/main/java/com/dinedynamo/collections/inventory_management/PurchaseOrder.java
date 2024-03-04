package com.dinedynamo.collections.inventory_management;


import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document("purchase_order")
public class PurchaseOrder
{
    @Id
    String purchaseOrderId;

    String restaurantId;

    String restaurantEmail;

    SupplierDetails supplierDetails;

    String itemName;

    double quantity;

    String measurementUnits;

    String description;

    LocalDate dateOfPurchaseRequest;

}
