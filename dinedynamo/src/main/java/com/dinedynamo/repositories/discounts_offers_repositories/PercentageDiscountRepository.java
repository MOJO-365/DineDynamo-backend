package com.dinedynamo.repositories.discounts_offers_repositories;

import com.dinedynamo.collections.discounts_offers.PercentageDiscount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PercentageDiscountRepository extends MongoRepository<PercentageDiscount, String> {

    Optional<List<PercentageDiscount>> findByRestaurantId(String restaurantId);
}
