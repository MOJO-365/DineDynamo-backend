package com.dinedynamo.collections.subscriptions_collections;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document("restaurant_subscriptions")
public class RestaurantSubscription {


    @Id
    String restaurantSubscriptionId;


    @Indexed(unique = true)
    String restaurantId;

    RestaurantSubscriptionStatus restaurantSubscriptionStatus;

    LocalDate startDate;

    LocalDate endDate;

    String subscriptionPlanId;



}

