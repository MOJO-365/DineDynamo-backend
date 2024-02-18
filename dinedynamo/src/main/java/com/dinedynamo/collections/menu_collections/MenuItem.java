package com.dinedynamo.collections.menu_collections;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document("menu_items")
public class MenuItem {

    @Id
    private String itemId;
    private String restaurantId;
    private String perentId;
    private String itemName;
    private String itemDescription;
    private double itemPrice;
    private String itemImgURL;
    private ParentType parentType;





}

