package com.dinedynamo.repositories;

import com.dinedynamo.collections.Order;
import com.dinedynamo.collections.Table;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


import javax.swing.text.html.Option;
import java.util.List;

public interface TableRepository extends MongoRepository<Table, String>
{

    @Query("{restaurantId: '?0'}")
    List<Table> findByRestaurantId(String restaurantId);


    @Query("{restaurantId: ?0, capacity: ?1}")
    List<Table> findByRestaurantIdAndCapacity(String restaurantId, int capacity);



}
