package com.dinedynamo.repositories.discounts_offers_repositories;

import com.dinedynamo.collections.discounts_offers.BogpOffer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BogpOfferRepository extends MongoRepository<BogpOffer, String> {

    Optional<List<BogpOffer>> findByRestaurantId(String restaurantId);
}
