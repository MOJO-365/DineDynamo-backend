package com.dinedynamo.dto;

import com.dinedynamo.collections.Restaurant;
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
