package com.dinedynamo.repositories;

import com.dinedynamo.collections.FinalBill;
import com.dinedynamo.collections.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinalBillRepository extends MongoRepository<FinalBill, String> {

    List<FinalBill> findByRestaurantId(String restaurantId);

    List<FinalBill> findByRestaurantIdAndOrderListItemId(String restaurantId, String itemId);

}
