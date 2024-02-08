package com.dinedynamo.repositories;


import com.dinedynamo.collections.Reservation;
import com.dinedynamo.collections.WaitingList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TableReservationRepository extends MongoRepository<Reservation, String>
{

    @Query("{restaurantId: '?0', guestCount: ?1}")
    Optional<List<Reservation>> findByRestaurantIdAndGuestCount(String restaurantId, int guestCount);

    @Query("{tableId: '?0',dineInDateAndTime: ?1}")
    Optional<Reservation> findByTableIdAndDineInDateAndTime(String tableId,String dineInDateAndTime);

    @Query("{restaurantId: '?0',customerPhone: ?1}")
    Optional<Reservation> findByRestaurantIdAndCustomerPhone(String restaurantId, String customerPhone);


    @Query("{'customerPhone' : ?0}")
    Optional<List<Reservation>> findByCustomerPhone(String customerPhone);
}
