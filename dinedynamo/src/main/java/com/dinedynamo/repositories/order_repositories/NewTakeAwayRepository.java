package com.dinedynamo.repositories.order_repositories;

import com.dinedynamo.collections.order_collections.TakeAway;
import com.dinedynamo.collections.report_collections.ItemSale;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface NewTakeAwayRepository extends MongoRepository<TakeAway,String> {

    List<TakeAway> findByRestaurantId(String restaurantId);

    long countByRestaurantId(String restaurantId);
    List<TakeAway> findByRestaurantIdAndDateTime(String restaurantId, LocalDateTime dateTime);

}


