package com.dinedynamo.repositories;

import com.dinedynamo.collections.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByRestaurantId(String restaurantId);


//    boolean existsByRestaurantIdAndCustomerNameAndStarRatingAndComment(String restaurantId, String customerName, int starRating, String comment);


}
