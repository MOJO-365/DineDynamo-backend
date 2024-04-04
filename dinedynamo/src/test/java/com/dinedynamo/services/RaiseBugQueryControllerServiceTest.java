package com.dinedynamo.services;
import com.dinedynamo.collections.restaurant_collections.BugQuery;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.repositories.restaurant_repositories.RestaurantRepository;
import com.dinedynamo.services.external_services.EmailService;
import com.dinedynamo.services.restaurant_services.RaiseBugQueryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RaiseBugQueryControllerServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private RaiseBugQueryService bugQueryService;

    @Test
    void testCreateAndSendQuery_ValidQuery() {
        BugQuery bugQuery = createSampleBugQuery();
        Restaurant restaurant = createSampleRestaurant();

        when(restaurantRepository.findById(bugQuery.getRestaurantId())).thenReturn(java.util.Optional.ofNullable(restaurant));
        when(emailService.sendBugQueryMail(any(), any(), any())).thenReturn(true);

        boolean result = bugQueryService.createAndSendQuery(bugQuery);

        assertTrue(result);
        // You can add additional assertions based on your specific requirements
    }

    @Test
    void testCreateAndSendQuery_EmptyBugDescription() {
        BugQuery bugQuery = createSampleBugQuery();
        bugQuery.setBugDescription("");
        boolean result = bugQueryService.createAndSendQuery(bugQuery);

        assertFalse(result);
    }

    @Test
    void testCreateAndSendQuery_NullBugDescription() {
        BugQuery bugQuery = createSampleBugQuery();
        bugQuery.setBugDescription("");
        boolean result = bugQueryService.createAndSendQuery(bugQuery);

        assertFalse(result);
    }

    @Test
    void testCreateAndSendQuery_NullRestaurantId() {
        BugQuery bugQuery = createSampleBugQuery();
        bugQuery.setRestaurantId(null);
        boolean result = bugQueryService.createAndSendQuery(bugQuery);

        assertFalse(result);
    }

    @Test
    void testCreateAndSendQuery_RestaurantNotFound() {
        BugQuery bugQuery = createSampleBugQuery();

        when(restaurantRepository.findById(bugQuery.getRestaurantId())).thenReturn(java.util.Optional.empty());

        boolean result = bugQueryService.createAndSendQuery(bugQuery);

        assertFalse(result);
    }

    @Test
    void testCreateAndSendQuery_EmailNotSent() {
        BugQuery bugQuery = createSampleBugQuery();
        Restaurant restaurant = createSampleRestaurant();

        when(restaurantRepository.findById(bugQuery.getRestaurantId())).thenReturn(java.util.Optional.ofNullable(restaurant));
        when(emailService.sendBugQueryMail(any(), any(), any())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> bugQueryService.createAndSendQuery(bugQuery));
    }

    // Add more tests for other scenarios as needed

    private BugQuery createSampleBugQuery() {
        BugQuery bugQuery = new BugQuery();
        bugQuery.setBugQueryTitle("Test Query");
        bugQuery.setBugDescription("Description of the bug");
        bugQuery.setRestaurantId("398");
        return bugQuery;
    }

    private Restaurant createSampleRestaurant() {
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId("398");
        restaurant.setRestaurantName("Test Restaurant");
        // Set other necessary fields
        return restaurant;
    }
}
