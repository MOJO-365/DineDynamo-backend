package com.dinedynamo.repositories;


import com.dinedynamo.collections.Reservation;
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

    @Query("{tableId: '?0',dineInDate: ?1}")
    Optional<Reservation> findByTableIdAndDineInDateAndTime(String tableId,LocalDateTime dineInDateAndTime);
}
