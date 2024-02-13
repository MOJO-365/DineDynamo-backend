package com.dinedynamo.dto;

import com.dinedynamo.collections.RestaurantReservationSettings;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class RestaurantReservationSettingsDTO {

    String RestaurantReservationSettingsId;

    RestaurantReservationSettings restaurantReservationSettings;
}
