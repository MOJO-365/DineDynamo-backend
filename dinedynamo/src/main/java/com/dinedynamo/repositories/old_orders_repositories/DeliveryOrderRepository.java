package com.dinedynamo.repositories.old_orders_repositories;

import com.dinedynamo.collections.old_order_collections.DeliveryOrder;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DeliveryOrderRepository extends MongoRepository<DeliveryOrder,String>{

    List<DeliveryOrder> findByRestaurantId(String restaurantId);

}
