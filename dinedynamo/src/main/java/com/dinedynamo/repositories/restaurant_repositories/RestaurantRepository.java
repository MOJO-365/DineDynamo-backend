package com.dinedynamo.repositories.restaurant_repositories;

import com.dinedynamo.collections.restaurant_collections.Restaurant;
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


    @Query("{ 'restaurantName' : { $regex: ?0, $options: 'i' } }")
    List<Restaurant> findByRestaurantNameRegexIgnoreCase(String regexPattern);

    @Query("{'resetToken': ?0}")
    Restaurant findByResetToken(String resetToken);

    @Query("{'isPureVeg': true, 'restaurantCity': ?0}")
    List<Restaurant> findAllPureVegRestaurantsByCity(String restaurantCity);

    @Query("{'isPureVeg': false, 'restaurantCity': ?0}")
    List<Restaurant> findAllNonPureVegRestaurantsByCity(String restaurantCity);



    List<Restaurant> findByRestaurantCityOrderByCostForTwoAsc(String restaurantCity);

    List<Restaurant> findByRestaurantCityOrderByCostForTwoDesc(String restaurantCity);

    List<Restaurant> findByRestaurantCityAndCostForTwoBetween(String restaurantCity, double minCost, double maxCost);

    List<Restaurant> findByRestaurantCityAndCostForTwoGreaterThan(String restaurantCity, double cost);

    List<Restaurant> findByRestaurantCityAndRestaurantRatingGreaterThan(String restaurantCity, double restaurantRating);



    // Combination 1: Pure Veg, Ascending Cost
    List<Restaurant> findByRestaurantCityAndIsPureVegOrderByCostForTwoAsc(String restaurantCity);

    // Combination 2: Pure Veg, Descending Cost
    List<Restaurant> findByRestaurantCityAndIsPureVegOrderByCostForTwoDesc(String restaurantCity);

    // Combination 3: Pure Veg, Cost Between
    List<Restaurant> findByRestaurantCityAndIsPureVegAndCostForTwoBetween(
            String restaurantCity, boolean isPureVeg, double minCost, double maxCost
    );

    // Combination 4: Pure Veg, Cost Greater Than
    List<Restaurant> findByRestaurantCityAndIsPureVegAndCostForTwoGreaterThan(
            String restaurantCity, boolean isPureVeg, double cost
    );

    // Combination 5: Pure Veg, Rating Greater Than
    List<Restaurant> findByRestaurantCityAndRestaurantRatingGreaterThanAndIsPureVeg(
            String restaurantCity, double rating, boolean isPureVeg
    );
}
