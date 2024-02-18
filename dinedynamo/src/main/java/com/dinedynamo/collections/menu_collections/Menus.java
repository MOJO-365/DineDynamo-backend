package com.dinedynamo.collections.menu_collections;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexOptions;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "menus")
public class Menus {

    @Id
    private String menuId;
    @Indexed(unique = true)
    private String restaurantId;
    private List<Category> listOfCategories;

}

