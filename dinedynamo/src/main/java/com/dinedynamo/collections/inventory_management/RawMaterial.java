package com.dinedynamo.collections.inventory_management;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RawMaterial {

    @Id
    String rawMaterialId;

    String restaurantId;

    String name;

    String description;

    String category;

    String measurementUnits;

    double currentLevel;

    double reorderLevel;

    Date purchaseDate;

    Date expirationDate;

    double costPerUnit;

    double totalCost;

//    @DBRef
//    SupplierDetails supplierDetails;
//
//
//    @DBRef
//    List<WastageLog> wastageLogList;
//
//
//    @DBRef
//    List<ReplenishmentLog> replenishmentLogList;

}
