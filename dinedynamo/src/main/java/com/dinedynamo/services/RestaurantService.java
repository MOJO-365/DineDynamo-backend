package com.dinedynamo.services;

import com.dinedynamo.collections.Restaurant;
import com.dinedynamo.repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public Page<Restaurant> getAllRestaurants(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return restaurantRepository.findAll(pageable);
    }


    public boolean validateRestaurantFieldsForSignUp(Restaurant restaurant){
        if(restaurant.getRestaurantEmail() == null || restaurant.getRestaurantPassword() == null){
            return false;
        }

        if(restaurant.getRestaurantName()== null || restaurant.getRestaurantCity() == null || restaurant.getRestaurantLocation() == null){
            return false;
        }

        return true;
    }
}
