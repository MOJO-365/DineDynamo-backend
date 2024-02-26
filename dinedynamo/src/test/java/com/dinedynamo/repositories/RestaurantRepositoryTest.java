package com.dinedynamo.repositories;

import com.dinedynamo.collections.authentication_collections.Restaurant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class RestaurantRepositoryTest {



    @Autowired
    private RestaurantRepository restaurantRepository;


    @Test
    void findByRestaurantEmail() {


        String restaurantEmail = "example@example.com";
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantEmail(restaurantEmail);
        restaurantRepository.save(restaurant);

        Optional<Restaurant> result = restaurantRepository.findByRestaurantEmail(restaurantEmail);

        assertTrue(result.isPresent());
        assertEquals(restaurant, result.get());

    }

    @Test
    void findByRestaurantId() {
        String restaurantId = "123";
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(restaurantId);
        restaurantRepository.save(restaurant);

        Restaurant result = restaurantRepository.findByRestaurantId(restaurantId);

        assertEquals(restaurant, result);
    }

    @Test
    void findByRestaurantName() {
        String restaurantId = "1234";
        String restaurantName = "rname";
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(restaurantId);
        restaurant.setRestaurantName(restaurantName);
        restaurantRepository.save(restaurant);
        Restaurant result = restaurantRepository.findByRestaurantName(restaurantName);
        assertEquals(restaurant,result);
    }

    @Test
    void findByRestaurantCity() {
        String restaurantId = "1234";
        String restaurantCity = "my_city";
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(restaurantId);
        restaurant.setRestaurantCity(restaurantCity);
        restaurantRepository.save(restaurant);
        List<Restaurant> result = restaurantRepository.findByRestaurantCity(restaurantCity);

        assertEquals(1, result.size());
        assertEquals(restaurant, result.get(0));
    }



    @Test
    void findByRestaurantNameRegexIgnoreCase() {
        String regexPattern = "example";
        Restaurant restaurant1 = new Restaurant();
        restaurant1.setRestaurantName("Example Restaurant");
        restaurantRepository.save(restaurant1);

        Restaurant restaurant2 = new Restaurant();
        restaurant2.setRestaurantName("Another Example");
        restaurantRepository.save(restaurant2);

        List<Restaurant> result = restaurantRepository.findByRestaurantNameRegexIgnoreCase(regexPattern);

        assertEquals(2, result.size());
    }
}