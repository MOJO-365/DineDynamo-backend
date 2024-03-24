package com.dinedynamo.repositories.subscription_repositories;


import com.dinedynamo.collections.subscriptions_collections.SubscriptionPlan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionPlanRepository extends MongoRepository<SubscriptionPlan, String> {
}
