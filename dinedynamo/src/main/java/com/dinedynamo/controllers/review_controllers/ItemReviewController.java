package com.dinedynamo.controllers.review_controllers;

import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.rating_and_review_collections.ItemReview;
import com.dinedynamo.repositories.review_and_rating_repositories.ItemReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
public class ItemReviewController {
    @Autowired
    private ItemReviewRepository itemReviewRepository;

    // Endpoint to add a new review
    @PostMapping("dinedynamo/item/review/add")
    public ResponseEntity<ApiResponse> addReview(@RequestBody ItemReview itemReview) {
        itemReviewRepository.save(itemReview);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED, "success", "Review added successfully"), HttpStatus.CREATED);
    }

    // Endpoint to get reviews by item
    @PostMapping("dinedynamo/item/review/get-reviews")
    public ResponseEntity<ApiResponse> getReviewsByItem(@RequestBody ItemReview itemReview) {
        String itemId = itemReview.getItemId();
        List<ItemReview> reviews = itemReviewRepository.findByItemId(itemId);
        ApiResponse response = new ApiResponse(HttpStatus.OK, "Reviews retrieved successfully", reviews);
        return ResponseEntity.ok(response);
    }


    // Endpoint to update an existing review
    @PostMapping("dinedynamo/item/review/update")
    public ResponseEntity<ApiResponse> updateReview(@RequestBody ItemReview itemReview) {
        Optional<ItemReview> optionalItemReview = itemReviewRepository.findById(itemReview.getFeedbackId());

        if (optionalItemReview.isPresent()) {
            itemReviewRepository.save(itemReview);
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", "Review updated successfully"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "failure", "Review not found"), HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint to delete a review
    @PostMapping("dinedynamo/item/review/delete")
    public ResponseEntity<ApiResponse> deleteReview(@RequestBody ItemReview itemReview) {
        Optional<ItemReview> optionalItemReview = itemReviewRepository.findById(itemReview.getFeedbackId());

        if (optionalItemReview.isPresent()) {
            itemReviewRepository.deleteById(itemReview.getFeedbackId());
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", "Review deleted successfully"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "failure", "Review not found"), HttpStatus.NOT_FOUND);
        }
    }
}
