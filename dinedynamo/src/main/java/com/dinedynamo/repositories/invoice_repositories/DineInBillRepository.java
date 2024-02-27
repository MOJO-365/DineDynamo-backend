package com.dinedynamo.repositories.invoice_repositories;

import com.dinedynamo.collections.invoice_collections.DineInFinalBill;
import com.dinedynamo.collections.order_collections.OrderList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DineInBillRepository extends MongoRepository<DineInFinalBill, String> {

    List<DineInFinalBill> findByRestaurantId(String restaurantId);

    List<DineInFinalBill> findByRestaurantIdAndOrderListItemId(String restaurantId, String itemId);
    OrderList findTopByRestaurantIdAndOrderListItemId(String restaurantId, String itemId);

}
