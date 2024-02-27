package com.dinedynamo.collections.inventory_management;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReplenishmentLog {


    @Id
    String replenishmentLogId;

    String operatorName;

    double replenishedQuantity;

    LocalDateTime timestamp;

    String replenishmentLocation;

    String restaurantId;
}
