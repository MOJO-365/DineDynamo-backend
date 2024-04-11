package com.dinedynamo.repositories.invoice_repositories;

import com.dinedynamo.collections.invoice_collections.DeliveryFinalBill;
import com.dinedynamo.collections.invoice_collections.DineInFinalBill;
import com.dinedynamo.collections.invoice_collections.TakeAwayFinalBill;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DeliveryFinalBillRepository extends MongoRepository<DeliveryFinalBill, String> {

    List<DeliveryFinalBill> findByRestaurantId(String restaurantId);

    List<DeliveryFinalBill> findByCustomerPhone(String customerPhone);


    long countByRestaurantId(String restaurantId);

    long countByRestaurantIdAndDate(String restaurantId,LocalDate date);


    List<DeliveryFinalBill> findByRestaurantIdAndDate(String restaurantId, LocalDate date);

}