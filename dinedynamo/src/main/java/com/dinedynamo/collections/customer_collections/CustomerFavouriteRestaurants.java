package com.dinedynamo.collections.customer_collections;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("favourite_restaurants")
public class CustomerFavouriteRestaurants
{
    @Id
    String customerPhone;
    List<String> listOfRestaurantIds;
}
