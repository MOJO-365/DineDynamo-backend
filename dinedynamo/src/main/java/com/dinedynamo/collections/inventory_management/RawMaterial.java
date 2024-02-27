package com.dinedynamo.collections.inventory_management;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

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

    String currentLevel;

    String reorderLevel;

    String purchaseDate;

    String expirationDate;

    double costPerUnit;

    double totalCost;

    @DBRef
    SupplierDetails supplierDetails;

}
