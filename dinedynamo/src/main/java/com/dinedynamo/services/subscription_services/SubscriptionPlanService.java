package com.dinedynamo.services.subscription_services;

import com.dinedynamo.collections.subscriptions_collections.SubscriptionPlan;
import com.dinedynamo.repositories.subscription_repositories.SubscriptionPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionPlanService
{
    @Autowired
    SubscriptionPlanRepository subscriptionPlanRepository;


    public SubscriptionPlan save(SubscriptionPlan subscriptionPlan){

        subscriptionPlanRepository.save(subscriptionPlan);
        return subscriptionPlan;
    }

    public SubscriptionPlan edit(String subscriptionPlanId, SubscriptionPlan subscriptionPlan){

        subscriptionPlan.setSubscriptionPlanId(subscriptionPlanId);
        subscriptionPlanRepository.save(subscriptionPlan);
        return subscriptionPlan;
    }
}
