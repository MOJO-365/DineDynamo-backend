package com.dinedynamo.repositories.review_and_rating_repositories;

import com.dinedynamo.collections.review_collections.RestaurantReview;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RestaurantReviewRepository extends MongoRepository<RestaurantReview, String> {
    List<RestaurantReview> findByRestaurantId(String restaurantId);


//    boolean existsByRestaurantIdAndCustomerNameAndStarRatingAndComment(String restaurantId, String customerName, int starRating, String comment);


}
