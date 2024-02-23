package com.dinedynamo.controllers;
import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.Restaurant;
import com.dinedynamo.collections.RestaurantReview;
import com.dinedynamo.repositories.RestaurantRepository;
import com.dinedynamo.repositories.RestaurantReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
public class RestaurantReviewController {
    @Autowired
    private RestaurantReviewRepository restaurantReviewRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    /**
     * Endpoint to add a review for a restaurant.
     * @param restaurantReview The review object containing review details.
     * @return ResponseEntity containing ApiResponse with success or failure message.
     */
    @PostMapping("dinedynamo/restaurant/reviews/add")
    public ResponseEntity<ApiResponse> addReview(@RequestBody RestaurantReview restaurantReview) {
        String restaurantId = restaurantReview.getRestaurantId();
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);

        if (optionalRestaurant.isPresent()) {
            restaurantReviewRepository.save(restaurantReview);
            return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED, "success","Review added successfully"), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "failure","Restaurant not found"), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint to get reviews for a specific restaurant.
     * @param restaurant The restaurant object for which reviews are requested.
     * @return ResponseEntity containing ApiResponse with the list of reviews retrieved.
     */
    @PostMapping("dinedynamo/restaurant/show/reviews")
    public ResponseEntity<ApiResponse> getReviewsByRestaurant(@RequestBody Restaurant restaurant) {
        List<RestaurantReview> restaurantReviews = restaurantReviewRepository.findByRestaurantId(restaurant.getRestaurantId());
        ApiResponse response = new ApiResponse(HttpStatus.OK, "Reviews retrieved successfully", restaurantReviews);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to update a review.
     * @param restaurantReview The review object containing updated review details.
     * @return ResponseEntity containing ApiResponse with success or failure message.
     */
    @PostMapping("dinedynamo/restaurant/update-review")
    public ResponseEntity<ApiResponse> updateReview(@RequestBody RestaurantReview restaurantReview) {
        Optional<RestaurantReview> optionalReview = restaurantReviewRepository.findById(restaurantReview.getFeedbackId());

        if (optionalReview.isPresent()) {
            RestaurantReview existingRestaurantReview = optionalReview.get();
            existingRestaurantReview.setStarRating(restaurantReview.getStarRating());
            existingRestaurantReview.setComment(restaurantReview.getComment());

            restaurantReviewRepository.save(existingRestaurantReview);
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", "Review updated successfully"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "failure", "Review not found"), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint to get the average rating and total ratings for a restaurant.
     * @param restaurant The restaurant object for which average rating and total ratings are requested.
     * @return ResponseEntity containing ApiResponse with average rating and total ratings.
     */
    @PostMapping("dinedynamo/restaurant/show/average-rating")
    public ResponseEntity<ApiResponse> getAverageRatingForRestaurant(@RequestBody Restaurant restaurant) {
        List<RestaurantReview> reviews = restaurantReviewRepository.findByRestaurantId(restaurant.getRestaurantId());

        Map<String, List<RestaurantReview>> reviewsByRestaurant = reviews.stream().collect(Collectors.groupingBy(RestaurantReview::getRestaurantId));

        double averageRating = reviewsByRestaurant.values().stream()
                .flatMap(List::stream)
                .mapToDouble(RestaurantReview::getStarRating)
                .average()
                .orElse(0.0);

        long totalRatings = reviews.size();

        Map<String, Object> averageRatings = Map.of("averageRating", averageRating, "totalRatings", totalRatings);

        ApiResponse response = new ApiResponse(HttpStatus.OK, "Average ratings and total ratings retrieved successfully", averageRatings);
        return ResponseEntity.ok(response);
    }


    /**
     * Endpoint to delete a review.
     * @param restaurantReview The review object to be deleted.
     * @return ResponseEntity containing ApiResponse with success or failure message.
     */
    @DeleteMapping("dinedynamo/restaurant/delete-rating")
    public ResponseEntity<ApiResponse> deleteRating(@RequestBody RestaurantReview restaurantReview){

        Optional<RestaurantReview> optionalReview = restaurantReviewRepository.findById(restaurantReview.getFeedbackId());

        if(optionalReview.isPresent()){

            restaurantReviewRepository.deleteById(restaurantReview.getFeedbackId());
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",null),HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"failure","review deleted"),HttpStatus.OK);
        }
    }


}
