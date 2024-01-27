package com.dinedynamo.repositories;

import com.dinedynamo.collections.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends MongoRepository<Restaurant, String>, PagingAndSortingRepository<Restaurant, String>
{
    @Query("{restaurantEmail:'?0'}")
    Optional<Restaurant> findByRestaurantEmail(String restaurantEmail);

    @Query("{restaurantId: '?0'}")
    Restaurant findByRestaurantId(String restaurantId);

    @Query("{restaurantName: '?0'}")
    Restaurant findByRestaurantName(String restaurantName);

    @Query("{restaurantCity: '?0'}")
    List<Restaurant> findByRestaurantCity(String restaurantCity);



    Page<Restaurant> findAll(Pageable pageable);



}
