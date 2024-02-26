package com.dinedynamo.repositories.old_orders_repositories;

import com.dinedynamo.collections.old_order_collections.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {


    List<Order> findByRestaurantId(String restaurantId);

    List<Order> findByTableId(String tableId);

//    @Query("{ 'orderList': ?0 }")
//    List<Order> findOrderByOrderList(JSONArray orderList);

}








