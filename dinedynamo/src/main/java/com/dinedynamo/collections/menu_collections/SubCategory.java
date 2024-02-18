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
@Document("subcategories")
public class SubCategory
{
    @Id
    private String subcategoryId;
    private String restaurantId;
    private String categoryId;
    private String subcategoryName;
    private List<SubSubCategory> listOfSubSubCategories;
    private List<MenuItem> listOfMenuItems;
}
