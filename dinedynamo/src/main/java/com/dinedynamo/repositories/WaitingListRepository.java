package com.dinedynamo.repositories;


import com.dinedynamo.collections.WaitingList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WaitingListRepository extends MongoRepository<WaitingList, String> {


    @Query("{ 'restaurantId' : ?0 }")
    Optional<List<WaitingList>> findByRestaurantId(String restaurantId);


    @Query("{ 'restaurantId' : ?0, 'customerPhone' : ?1}")
    Optional<WaitingList> findByRestaurantIdAndCustomerPhone(String restaurantId, String customerPhone);


    @Query("{'customerPhone' : ?0}")
    Optional<List<WaitingList>> findByCustomerPhone(String customerPhone);
}
