package com.dinedynamo.dto.subscription_dtos;


import com.dinedynamo.collections.subscriptions_collections.SubscriptionPlan;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EditSubscriptionPlanDTO {
    String subscriptionPlanId;

    SubscriptionPlan subscriptionPlan;
}
