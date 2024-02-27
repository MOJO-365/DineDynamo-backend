package com.dinedynamo.collections.inventory_management;


import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WastageLog
{
    @Id
    String wastageLogId;



    String wastageReason;

    String operatorName;

    double wastedQuantity;

    String wastageLocation;

    LocalDateTime timestamp;

    String restaurantId;

}
