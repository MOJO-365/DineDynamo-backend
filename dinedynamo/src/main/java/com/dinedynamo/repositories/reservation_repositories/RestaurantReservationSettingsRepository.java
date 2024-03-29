package com.dinedynamo.repositories.reservation_repositories;

import com.dinedynamo.collections.reservation_collections.RestaurantReservationSettings;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantReservationSettingsRepository extends MongoRepository<RestaurantReservationSettings, String> {



    @Query("{ 'restaurantId' : '?0' }")
    Optional<RestaurantReservationSettings> findByRestaurantId(String restaurantId);


}
