package com.dinedynamo.repositories.order_repositories;


import com.dinedynamo.collections.order_collections.Order;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface DineInOrderRepository extends MongoRepository<Order, String> {


    List<Order> findByRestaurantId(String restaurantId);

    List<Order> findByTableId(String tableId);


}
