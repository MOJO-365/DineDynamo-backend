package com.dinedynamo.collections.table_collections;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document("tables")
@CompoundIndexes({
        @CompoundIndex(name = "restaurant_category_table", def = "{'restaurantId': 1, 'tableCategory': 1}", unique = true)
})
public class Table
{
    @Id
    String tableId;
    String restaurantId;
    @Indexed(unique = true)
    String tableName;
    int capacity;
    int coordinateX;
    int coordinateY;
    String status;
    Boolean isPositionAbsolute;
    String tableCategory;
    String tableQRURL;
    String publicIdOfQRImage;

}

