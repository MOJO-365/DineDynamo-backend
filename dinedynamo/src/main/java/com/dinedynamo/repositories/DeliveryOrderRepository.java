package com.dinedynamo.repositories;

import com.dinedynamo.collections.DeliveryOrder;
import com.dinedynamo.collections.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DeliveryOrderRepository extends MongoRepository<DeliveryOrder,String>{

    List<DeliveryOrder> findByRestaurantId(String restaurantId);

}
