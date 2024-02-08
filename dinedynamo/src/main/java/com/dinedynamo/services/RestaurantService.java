package com.dinedynamo.services;

import com.dinedynamo.collections.Restaurant;
import com.dinedynamo.repositories.RestaurantRepository;
import jakarta.servlet.http.PushBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RestaurantService {

    @Autowired
    RestaurantRepository restaurantRepository;

<<<<<<< HEAD



    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }
=======
//    @Autowired
//    public RestaurantService(RestaurantRepository restaurantRepository) {
//        this.restaurantRepository = restaurantRepository;
//    }
>>>>>>> 6567fd74d5dd09cc1769a6c70b3723cb570127f5

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


    public boolean isRestaurantPresentinDb(String restaurantId){

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        if(restaurant == null){
            return false;
        }
        return true;
    }
}
