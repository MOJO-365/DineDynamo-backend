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


    // false when the restaurant subscribes for the first time, true when the restaurant has extends (takes new subscription)
    boolean isRenewed;


}

