package com.dinedynamo.repositories.invoice_repositories;

import com.dinedynamo.collections.invoice_collections.DineInFinalBill;
import com.dinedynamo.collections.invoice_collections.TakeAwayFinalBill;
import com.dinedynamo.collections.order_collections.OrderList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DineInFinalBillRepository extends MongoRepository<DineInFinalBill, String> {

    List<DineInFinalBill> findByRestaurantId(String restaurantId);

    long countByRestaurantId(String restaurantId);

    List<DineInFinalBill> findByRestaurantIdAndDate(String restaurantId, LocalDate date);

}
