package com.dinedynamo.collections.menu_collections;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document("subsubcategories")
public class SubSubCategory
{

    @Id
    private String subSubcategoryId;
    private String restaurantId;
    private String subSubcategoryName;
    private String subcategoryId;
    private List<MenuItem> listOfMenuItems;

}
