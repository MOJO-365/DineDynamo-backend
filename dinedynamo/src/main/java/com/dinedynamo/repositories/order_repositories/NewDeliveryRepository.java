package com.dinedynamo.repositories.order_repositories;

import com.dinedynamo.collections.order_collections.DeliveryOrder;
import com.dinedynamo.collections.report_collections.ItemSale;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface NewDeliveryRepository extends MongoRepository<DeliveryOrder,String> {

    List<DeliveryOrder> findByRestaurantId(String restaurantId);


    long countByRestaurantId(String restaurantId);



    @Query(value = "{ 'restaurantId' : ?0, 'dateTime' : ?1 }", fields = "{ 'orderList' : 1 }")
    List<ItemSale> findItemsByRestaurantIdAndDate(String restaurantId, LocalDate date);
}
