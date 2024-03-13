package com.dinedynamo.repositories.order_repositories;

import com.dinedynamo.collections.order_collections.TakeAway;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TakeAwayOrderRepository extends MongoRepository<TakeAway,String> {

    List<TakeAway> findByRestaurantId(String restaurantId);
    ;

}


