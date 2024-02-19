package com.dinedynamo.repositories.order_repositories;

import com.dinedynamo.collections.order_collections.DeliveryOrder;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NewDeliveryRepository extends MongoRepository<DeliveryOrder,String> {

    List<DeliveryOrder> findByRestaurantId(String restaurantId);

}
