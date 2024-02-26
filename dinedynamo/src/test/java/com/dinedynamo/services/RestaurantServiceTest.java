package com.dinedynamo.services;

import com.dinedynamo.collections.authentication_collections.Restaurant;
import com.dinedynamo.repositories.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @InjectMocks
    RestaurantService restaurantService;

    @Mock
    RestaurantRepository restaurantRepository;

    @Test
    void testValidateRestaurantFieldsForSignUp_Valid() {
        Restaurant restaurant = createValidRestaurant();

        boolean result = restaurantService.validateRestaurantFieldsForSignUp(restaurant);

        assertTrue(result);
    }

    @Test
    void testValidateRestaurantFieldsForSignUp_Invalid() {
        Restaurant restaurant = new Restaurant();

        boolean result = restaurantService.validateRestaurantFieldsForSignUp(restaurant);

        assertFalse(result);
    }


    @Test
    void testIsRestaurantPresentInDb_Present() {
        String restaurantId = "123";
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(new Restaurant()));

        boolean result = restaurantService.isRestaurantPresentinDb(restaurantId);

        assertTrue(result);
    }

    @Test
    void testIsRestaurantPresentInDb_NotPresent() {
        String restaurantId = "123";
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        boolean result = restaurantService.isRestaurantPresentinDb(restaurantId);

        assertFalse(result);
    }

    private Restaurant createValidRestaurant() {
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantEmail("example@example.com");
        restaurant.setRestaurantPassword("password");
        restaurant.setRestaurantName("Example_Restaurant");
        restaurant.setRestaurantCity("Example_City");
        restaurant.setRestaurantLocation("Example_Location");

        return restaurant;
    }
}