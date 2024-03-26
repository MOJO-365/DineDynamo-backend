package com.dinedynamo.dto.subscription_dtos;

import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.collections.subscriptions_collections.RestaurantSubscription;
import com.dinedynamo.collections.subscriptions_collections.SubscriptionPlan;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ViewRestaurantSubscriptionDTO {

    Restaurant restaurant;

    SubscriptionPlan subscriptionPlan;

    RestaurantSubscription restaurantSubscription;


}
