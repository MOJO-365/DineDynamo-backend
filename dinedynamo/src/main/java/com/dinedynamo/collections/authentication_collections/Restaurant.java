package com.dinedynamo.collections.authentication_collections;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.TimeZone;


@Builder
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

    private String resetToken;

    String restaurantLocation;

    String restaurantCity;

    String restaurantHighlight;

    double restaurantRating;

    String startTime;

    String endTime;

    @Builder.Default
    double costForTwo=0.0;

    boolean isPureVeg;


    String restaurantABN;

    String restaurantPhone;



}
