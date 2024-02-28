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
@Document("wastage_logs")
public class WastageLog
{
    @Id
    String wastageLogId;

    String rawMaterialId;

    String restaurantId;

    String wastageReason;

    String operatorName;

    double wastedQuantity;

    String wastageLocation;

    LocalDateTime timestamp;



}
