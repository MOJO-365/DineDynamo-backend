package com.dinedynamo.repositories.invoice_repositories;

import com.dinedynamo.collections.payment_collections.SuccessfulPayment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SavePaymentsInDBRepository extends MongoRepository<SuccessfulPayment, String>
{

    @Query("{customerPhone:'?0'}")
    Optional<SuccessfulPayment> findByCustomerPhone(String customerPhone);

    @Query("{restaurantId:'?0'}")
    Optional<List<SuccessfulPayment>> findByRestaurantId(String restaurantId);
}
