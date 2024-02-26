package com.dinedynamo.repositories;

import com.dinedynamo.collections.old_order_collections.TakeAway;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TakeAwayOrderRepository extends MongoRepository<TakeAway,String> {

    List<TakeAway> findByRestaurantId(String restaurantId);

}
