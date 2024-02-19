package com.dinedynamo.collections;

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
@Document(collection = "item_reviews")
public class ItemReview {


    @Id
    private String feedbackId;
    private String restaurantId;
    private String itemId;
    private String customerName;
    private double starRating;
    private String comment;

}
