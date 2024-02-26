package com.dinedynamo.dto.restaurant_dtos;

import com.dinedynamo.collections.authentication_collections.Restaurant;
import lombok.*;


@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EditRestaurantDTO
{
    String restaurantId;
    Restaurant restaurant;
}
