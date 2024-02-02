package com.dinedynamo.repositories;

import com.dinedynamo.collections.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order, String> {


    List<Order> findByRestaurantId(String restaurantId);
    List<Order> findByRestaurantIdAndTableId(String restaurantId, String tableId);

    List<Order> findByTableId(String tableId);


}






