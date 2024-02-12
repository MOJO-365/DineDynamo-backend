package com.dinedynamo.controllers;

import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.Restaurant;
import com.dinedynamo.collections.Review;
import com.dinedynamo.repositories.RestaurantRepository;
import com.dinedynamo.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
public class ReviewController {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    @PostMapping("dinedynamo/restaurant/reviews/add")
    public ResponseEntity<ApiResponse> addReview(@RequestBody Review review) {
        String restaurantId = review.getRestaurantId();
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);

        if (optionalRestaurant.isPresent()) {
            reviewRepository.save(review);
            return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED, "success","Review added successfully"), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "failure","Restaurant not found"), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("dinedynamo/restaurant/show/reviews")
    public ResponseEntity<ApiResponse> getReviewsByRestaurant(@RequestBody Restaurant restaurant) {
        List<Review> reviews = reviewRepository.findByRestaurantId(restaurant.getRestaurantId());
        ApiResponse response = new ApiResponse(HttpStatus.OK, "Reviews retrieved successfully", reviews);
        return ResponseEntity.ok(response);
    }

    @PostMapping("dinedynamo/restaurant/show/average-rating")
    public ResponseEntity<ApiResponse> getAverageRatingForRestaurant(@RequestBody Restaurant restaurant) {
        List<Review> reviews = reviewRepository.findByRestaurantId(restaurant.getRestaurantId());

        Map<String, List<Review>> reviewsByRestaurant = reviews.stream().collect(Collectors.groupingBy(Review::getRestaurantId));

        Map<String, Map<String, Object>> averageRatingsByRestaurant = new HashMap<>();
        for (Map.Entry<String, List<Review>> entry : reviewsByRestaurant.entrySet()) {
            String restaurantId = entry.getKey();
            List<Review> restaurantReviews = entry.getValue();

            OptionalDouble average = restaurantReviews.stream()
                    .mapToDouble(Review::getStarRating)
                    .average();
            double averageRating = average.orElse(0.0);

            long totalRatings = restaurantReviews.size();

            Map<String, Object> restaurantData = Map.of("averageRating", averageRating, "totalRatings", totalRatings);

            averageRatingsByRestaurant.put(restaurantId, restaurantData);
        }

        ApiResponse response = new ApiResponse(HttpStatus.OK, "Average ratings and total ratings retrieved successfully", averageRatingsByRestaurant);
        return ResponseEntity.ok(response);
    }



    @DeleteMapping("dinedynamo/restaurant/delete-rating")
    public ResponseEntity<ApiResponse> deleteRating(@RequestBody Review review){

        Optional<Review> optionalReview = reviewRepository.findById(review.getFeedbackId());

        if(optionalReview.isPresent()){

            reviewRepository.deleteById(review.getFeedbackId());
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",null),HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"failure","review deleted"),HttpStatus.OK);
        }
    }

}
