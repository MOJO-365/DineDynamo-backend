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
    private String subSubCategoryId;
    private String restaurantId;
    private String subSubCategoryName;
    private String subCategoryId;
    private List<MenuItem> listOfMenuItems;

}
