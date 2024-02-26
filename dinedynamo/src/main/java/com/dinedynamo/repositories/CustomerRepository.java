package com.dinedynamo.repositories;

import com.dinedynamo.collections.authentication_collections.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String>
{

    @Query("{customerEmail:'?0'}")
    Optional<Customer> findByCustomerEmail(String customerEmail);

    @Query("{customerName:'?0'}")
    Customer findByCustomerName(String customerName);

    @Query("{customerId:'?0'}")
    Customer findByCustomerId(String customerId);

    @Query("{customerPhone:'?0'}")
    Customer findByCustomerPhone(String customerPhone);


}
