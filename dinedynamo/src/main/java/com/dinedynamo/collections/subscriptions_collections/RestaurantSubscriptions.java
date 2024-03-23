package com.dinedynamo.collections.subscriptions_collections;


import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RestaurantSubscriptions {


    @Id
    String restaurantSubscriptionId;

    String restaurantId;

    RestaurantSubscriptionStatus restaurantSubscriptionStatus;

    LocalDate startDate;

    LocalDate endDate;

    String subscriptionPlanId;



}

