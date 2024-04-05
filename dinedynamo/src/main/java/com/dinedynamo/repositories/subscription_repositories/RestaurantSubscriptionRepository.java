package com.dinedynamo.repositories.subscription_repositories;

import com.dinedynamo.collections.subscriptions_collections.RestaurantSubscription;
import com.dinedynamo.collections.subscriptions_collections.RestaurantSubscriptionStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;


@Repository
public interface RestaurantSubscriptionRepository extends MongoRepository<RestaurantSubscription, String> {


    List<RestaurantSubscription> findByRestaurantSubscriptionStatus(RestaurantSubscriptionStatus status);

    Optional<RestaurantSubscription> findByRestaurantId(String restaurantId);
}
