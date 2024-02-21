package com.dinedynamo.collections.menu_collections;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document("categories")
public class Category {

    @Id
    private String categoryId;
    private String restaurantId;
    private String categoryName;

    private  List<SubCategory> listOfSubCategories;
    private List<MenuItem> listOfMenuItems;

}
