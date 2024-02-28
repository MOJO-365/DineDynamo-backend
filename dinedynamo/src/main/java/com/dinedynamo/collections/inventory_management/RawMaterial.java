package com.dinedynamo.collections.inventory_management;


import lombok.*;
import org.springframework.data.annotation.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;


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

    LocalDate purchaseDate;

    LocalDate expirationDate;

    double costPerUnit;

    double totalCost;

    LocalDateTime timestamp;

    String operatorName;
}
