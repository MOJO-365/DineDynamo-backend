package com.dinedynamo.collections.rating_and_review_collections;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "restaurant_reviews")
public class RestaurantReview
{

    @Id
    private String feedbackId;
    private String restaurantId;
    private String customerName;
    private double starRating;
    private String comment;


}
