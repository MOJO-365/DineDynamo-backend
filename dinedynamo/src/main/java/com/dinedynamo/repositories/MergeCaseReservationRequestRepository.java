package com.dinedynamo.repositories;


import com.dinedynamo.collections.MergeCaseReservationRequest;
import com.dinedynamo.collections.WaitingList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface MergeCaseReservationRequestRepository extends MongoRepository<MergeCaseReservationRequest, String> {


    @Query("{customerPhone: '?0', restaurantId: '?1'}")
    Optional<MergeCaseReservationRequest> findByCustomerPhoneAndRestaurantId(String customerPhone, String restaurantId);



    @Query("{'customerPhone' : ?0}")
    Optional<List<MergeCaseReservationRequest>> findByCustomerPhone(String customerPhone);
}
