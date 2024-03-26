package com.dinedynamo.dto.subscription_dtos;


import com.dinedynamo.collections.subscriptions_collections.RestaurantSubscription;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SubscriptionResponseDTO {

    RestaurantSubscription restaurantSubscription;

    boolean isSavedToDb;

    boolean isPaymentDone;
}
