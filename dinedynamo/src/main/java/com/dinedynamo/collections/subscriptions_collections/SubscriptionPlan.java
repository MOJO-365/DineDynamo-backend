package com.dinedynamo.collections.subscriptions_collections;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("subscription_plans")
public class SubscriptionPlan
{

    @Id
    String subscriptionPlanId;

    double noOfMonths;

    double amount;



}
