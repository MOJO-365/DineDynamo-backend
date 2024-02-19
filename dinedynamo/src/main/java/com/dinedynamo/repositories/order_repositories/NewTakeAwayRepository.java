package com.dinedynamo.repositories.order_repositories;

import com.dinedynamo.collections.order_collections.TakeAway;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NewTakeAwayRepository extends MongoRepository<TakeAway,String> {

    List<TakeAway> findByRestaurantId(String restaurantId);



 }
