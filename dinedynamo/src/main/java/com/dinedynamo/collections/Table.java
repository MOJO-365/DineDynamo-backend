package com.dinedynamo.collections;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document("tables")
public class Table
{
    @Id
    String tableId;
    String restaurantId;
    String tableName;
    int capacity;
    int coordinateX;
    int coordinateY;
    String status;
    Boolean isPositionAbsolute;

    String tableQRURL;
    String publicIdOfQRImage;

}

