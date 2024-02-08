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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;

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
            return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED, "Review added successfully"), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "Restaurant not found"), HttpStatus.NOT_FOUND);
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
        OptionalDouble average = reviews.stream()
                .mapToDouble(Review::getRating)
                .average();
        double averageRating = average.orElse(0.0);
        ApiResponse response = new ApiResponse(HttpStatus.OK, "Average rating retrieved successfully", averageRating);
        return ResponseEntity.ok(response);
    }
}
