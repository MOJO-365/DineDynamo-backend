package com.dinedynamo.repositories.order_repositories;

import com.dinedynamo.collections.order_collections.DeliveryOrder;
import com.dinedynamo.collections.report_collections.ItemSale;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface DeliveryOrderRepository extends MongoRepository<DeliveryOrder,String> {

    List<DeliveryOrder> findByRestaurantId(String restaurantId);


}
