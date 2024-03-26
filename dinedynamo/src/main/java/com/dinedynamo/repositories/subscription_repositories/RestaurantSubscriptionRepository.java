package com.dinedynamo.repositories.subscription_repositories;

import com.dinedynamo.collections.subscriptions_collections.RestaurantSubscription;
import com.dinedynamo.collections.subscriptions_collections.RestaurantSubscriptionStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RestaurantSubscriptionRepository extends MongoRepository<RestaurantSubscription, String> {


    List<RestaurantSubscription> findByRestaurantSubscriptionStatus(RestaurantSubscriptionStatus status);
}
