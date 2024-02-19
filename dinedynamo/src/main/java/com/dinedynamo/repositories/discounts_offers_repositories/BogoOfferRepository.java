package com.dinedynamo.repositories.discounts_offers_repositories;

import com.dinedynamo.collections.discounts_offers.BogoOffer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BogoOfferRepository extends MongoRepository<BogoOffer, String> {
    Optional<List<BogoOffer>> findByRestaurantId(String restaurantId);
}
