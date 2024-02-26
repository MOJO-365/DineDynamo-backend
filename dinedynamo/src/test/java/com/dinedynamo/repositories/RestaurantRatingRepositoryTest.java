package com.dinedynamo.repositories;

import com.dinedynamo.collections.RestaurantReview;
import com.dinedynamo.repositories.RestaurantReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class RestaurantRatingRepositoryTest {

    @Mock
    RestaurantReviewRepository restaurantReviewRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        List<RestaurantReview> reviewsForRestaurant1 = new ArrayList<>();
        reviewsForRestaurant1.add(new RestaurantReview());
        reviewsForRestaurant1.add(new RestaurantReview());

        List<RestaurantReview> reviewsForRestaurant2 = new ArrayList<>();
        reviewsForRestaurant2.add(new RestaurantReview());
        reviewsForRestaurant2.add(new RestaurantReview());

        when(restaurantReviewRepository.findByRestaurantId("123")).thenReturn(reviewsForRestaurant1);
        when(restaurantReviewRepository.findByRestaurantId("456")).thenReturn(reviewsForRestaurant2);
    }

    @Test
    public void testFindByRestaurantId() {
        String restaurantId1 = "123";
        String restaurantId2 = "456";

        List<RestaurantReview> reviewsForRestaurant1 = restaurantReviewRepository.findByRestaurantId(restaurantId1);
        List<RestaurantReview> reviewsForRestaurant2 = restaurantReviewRepository.findByRestaurantId(restaurantId2);

        assertNotNull(reviewsForRestaurant1);
        assertNotNull(reviewsForRestaurant2);

        assertEquals(2, reviewsForRestaurant1.size());
        assertEquals(2, reviewsForRestaurant2.size());
    }
}
