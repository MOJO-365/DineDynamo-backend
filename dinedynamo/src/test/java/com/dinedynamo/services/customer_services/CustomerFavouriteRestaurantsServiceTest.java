package com.dinedynamo.services.customer_services;
import com.dinedynamo.collections.customer_collections.CustomerFavouriteRestaurants;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.repositories.restaurant_repositories.RestaurantRepository;
import com.dinedynamo.repositories.order_repositories.CustomerFavouriteRestaurantsRespository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerFavouriteRestaurantsServiceTest {

    @Mock
    private CustomerFavouriteRestaurantsRespository customerFavouriteRestaurantsRespository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private CustomerFavouriteRestaurantsService customerFavouriteRestaurantsService;

    private Restaurant createRestaurant(String id, String name, String email, String password, String resetToken, String location,
                                        String city, String highlight, double rating, LocalTime startTime, LocalTime endTime,
                                        double costForTwo, boolean isPureVeg, String abn, String phone) {
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(id);
        restaurant.setRestaurantName(name);
        restaurant.setRestaurantEmail(email);
        restaurant.setRestaurantPassword(password);
        restaurant.setResetToken(resetToken);
        restaurant.setRestaurantLocation(location);
        restaurant.setRestaurantCity(city);
        restaurant.setRestaurantHighlight(highlight);
        restaurant.setRestaurantRating(rating);
        restaurant.setStartTime(startTime);
        restaurant.setEndTime(endTime);
        restaurant.setCostForTwo(costForTwo);
        restaurant.setPureVeg(isPureVeg);
        restaurant.setRestaurantABN(abn);
        restaurant.setRestaurantPhone(phone);
        return restaurant;
    }

    @Test
    void testAddToFavourites() {

        // Arrange
        String customerPhone = "1234567890";
        String restaurantId = "abc123";

        CustomerFavouriteRestaurants existingFavourites = new CustomerFavouriteRestaurants();
        existingFavourites.setCustomerPhone(customerPhone);
        existingFavourites.setListOfRestaurantIds(new ArrayList<>());

        when(customerFavouriteRestaurantsRespository.findById(customerPhone)).thenReturn(Optional.of(existingFavourites));
        //when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(new Restaurant()));

        // Act
        CustomerFavouriteRestaurants result = customerFavouriteRestaurantsService.addToFavourites(customerPhone, restaurantId);

        // Assert
        verify(customerFavouriteRestaurantsRespository, times(1)).save(any());
        assertEquals(customerPhone, result.getCustomerPhone());
        assertEquals(1, result.getListOfRestaurantIds().size());
        assertEquals(restaurantId, result.getListOfRestaurantIds().get(0));


        // Arrange
//        String customerPhone = "12345678911";
//        String restaurantId = "abc123";
//        CustomerFavouriteRestaurants existingFavourites = new CustomerFavouriteRestaurants();
//        existingFavourites.setCustomerPhone(customerPhone);
//        existingFavourites.setListOfRestaurantIds(new ArrayList<>());
//
//        when(customerFavouriteRestaurantsRespository.findById(customerPhone)).thenReturn(Optional.of(existingFavourites));
//        when(restaurantRepository.findById("abc123")).thenReturn(Optional.of(createRestaurant("abc123", "Restaurant1", "test@example.com", "password", "resetToken", "Test Location", "Test City", "Test Highlight", 4.5, LocalTime.of(8, 0), LocalTime.of(22, 0), 0.0, true, "123456789", "987654321")));
//
//        // Act
//        CustomerFavouriteRestaurants result = customerFavouriteRestaurantsService.addToFavourites(customerPhone, restaurantId);
//
//        // Assert
//        verify(customerFavouriteRestaurantsRespository, times(1)).save(any());
//        assertEquals(customerPhone, result.getCustomerPhone());
//        assertEquals(1, result.getListOfRestaurantIds().size());
//        assertEquals(restaurantId, result.getListOfRestaurantIds().get(0));
    }

    @Test
    void testGetAllFavourites() {
        // Arrange
        String customerPhone = "1234567890";
        CustomerFavouriteRestaurants existingFavourites = new CustomerFavouriteRestaurants();
        existingFavourites.setCustomerPhone(customerPhone);
        existingFavourites.setListOfRestaurantIds(List.of("abc123", "def456"));

        when(customerFavouriteRestaurantsRespository.findById(customerPhone)).thenReturn(Optional.of(existingFavourites));
        when(restaurantRepository.findById("abc123")).thenReturn(Optional.of(createRestaurant("abc123", "Restaurant1", "test@example.com", "password", "resetToken", "Test Location", "Test City", "Test Highlight", 4.5, LocalTime.of(8, 0), LocalTime.of(22, 0), 0.0, true, "123456789", "987654321")));
        when(restaurantRepository.findById("def456")).thenReturn(Optional.of(createRestaurant("def456", "Restaurant2", "test2@example.com", "password2", "resetToken2", "Test Location 2", "Test City 2", "Test Highlight 2", 4.0, LocalTime.of(9, 0), LocalTime.of(23, 0), 0.0, false, "987654321", "123456789")));

        // Act
        List<Restaurant> result = customerFavouriteRestaurantsService.getAllFavourites(customerPhone);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Restaurant1", result.get(0).getRestaurantName());
        assertEquals("Restaurant2", result.get(1).getRestaurantName());
    }

    @Test
    void testDeleteFromFavourites() {
        String customerPhone = "1234567890";
        String restaurantIdToDelete = "abc123";

        // Assuming CustomerFavouriteRestaurants has a mutable list
        List<String> listOfFavs = new ArrayList<>();
        listOfFavs.add("abc123");
        listOfFavs.add("def456");

        CustomerFavouriteRestaurants existingFavourites = new CustomerFavouriteRestaurants();
        existingFavourites.setCustomerPhone(customerPhone);
        existingFavourites.setListOfRestaurantIds(listOfFavs);

        when(customerFavouriteRestaurantsRespository.findById(customerPhone)).thenReturn(Optional.of(existingFavourites));
//        when(restaurantRepository.findById("abc123")).thenReturn(Optional.of(new Restaurant()));

        // Act
        List<String> result = customerFavouriteRestaurantsService.deleteFromFavourites(customerPhone, restaurantIdToDelete);

        // Assert
        verify(customerFavouriteRestaurantsRespository, times(1)).save(any());
        assertEquals(1, result.size());
        assertEquals("def456", result.get(0));
    }
}
