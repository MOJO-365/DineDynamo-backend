package com.dinedynamo.repositories.invoice_repositories;

import com.dinedynamo.collections.invoice_collections.DeliveryFinalBill;
import com.dinedynamo.collections.invoice_collections.TakeAwayFinalBill;
import com.dinedynamo.collections.order_collections.TakeAway;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface TakeAwayFinalBillRepository extends MongoRepository<TakeAwayFinalBill,String> {

    long countByRestaurantId(String restaurantId);


    List<TakeAwayFinalBill> findByRestaurantId(String restaurantId);
    List<TakeAwayFinalBill> findByRestaurantIdAndDate(String restaurantId, LocalDate date);


}
