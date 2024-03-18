package com.dinedynamo.repositories.invoice_repositories;

import com.dinedynamo.collections.invoice_collections.DeliveryFinalBill;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryFinalBillRepository extends MongoRepository<DeliveryFinalBill, String> {

    long countByRestaurantId(String restaurantId);


}