package com.dinedynamo.collections.inventory_management;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document("replenishment_logs")
public class ReplenishmentLog {


    @Id
    String replenishmentLogId;

    String restaurantId;

    String rawMaterialId;

    String operatorName;

    double replenishedQuantity;

    LocalDateTime timestamp;

    String replenishmentLocation;

}
