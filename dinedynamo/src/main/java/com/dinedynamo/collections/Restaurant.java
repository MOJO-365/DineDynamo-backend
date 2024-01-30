package com.dinedynamo.collections;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document("restaurant")
public class Restaurant
{
    @Id
    String restaurantId;


    String restaurantName;
    @Indexed(unique = true)

    String restaurantEmail;

    String restaurantPassword;

    String restaurantLocation;

    String restaurantCity;

    String restaurantHighlight;

    double restaurantRating;

    int restaurantppr;

    LocalDateTime startTime;

    LocalDateTime endTime;

    String termsAndConditionsForTableReservation;


}
