package com.dinedynamo.services.customer_services;


import com.dinedynamo.collections.customer_collections.CustomerFavouriteRestaurants;
import com.dinedynamo.repositories.restaurant_repositories.RestaurantRepository;
import com.dinedynamo.repositories.order_repositories.CustomerFavouriteRestaurantsRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import java.util.List;

@Service
public class CustomerFavouriteRestaurantsService {

    @Autowired
    CustomerFavouriteRestaurantsRespository customerFavouriteRestaurantsRespository;

    @Autowired
    RestaurantRepository restaurantRepository;


    public CustomerFavouriteRestaurants addToFavourites(String customerPhone, String restaurantId){

        CustomerFavouriteRestaurants customerFavouriteRestaurants = customerFavouriteRestaurantsRespository.findById(customerPhone).orElse(null);

        if(customerFavouriteRestaurants != null){
            List<String> favourites = customerFavouriteRestaurants.getListOfRestaurantIds();
            favourites.add(restaurantId);
            customerFavouriteRestaurants.setCustomerPhone(customerPhone);
            customerFavouriteRestaurants.setListOfRestaurantIds(favourites);
            customerFavouriteRestaurantsRespository.save(customerFavouriteRestaurants);
        }

        else{
            customerFavouriteRestaurants = new CustomerFavouriteRestaurants();
            customerFavouriteRestaurants.setCustomerPhone(customerPhone);
            List<String> favourites = new ArrayList<>();
            favourites.add(restaurantId);
            customerFavouriteRestaurants.setListOfRestaurantIds(favourites);
            customerFavouriteRestaurantsRespository.save(customerFavouriteRestaurants);
        }
        return customerFavouriteRestaurants;
    }

    public List<Restaurant> getAllFavourites(String customerPhone){
        CustomerFavouriteRestaurants customerFavouriteRestaurants = customerFavouriteRestaurantsRespository.findById(customerPhone).orElse(null);

        //return customerFavouriteRestaurants.getListOfRestaurantIds();
        List<Restaurant> restaurantList = new ArrayList<>();

       if(customerFavouriteRestaurants == null){
           return new ArrayList<>();
       }


        for(String restaurantId: customerFavouriteRestaurants.getListOfRestaurantIds()){

            restaurantList.add(restaurantRepository.findById(restaurantId).orElse(null));
        }
        return restaurantList;
    }

    public List<String> deleteFromFavourites(String customerPhone, String restaurantId){

        List<String> listOfFavs = customerFavouriteRestaurantsRespository.findById(customerPhone).get().getListOfRestaurantIds();

        listOfFavs.remove(restaurantId);

        CustomerFavouriteRestaurants customerFavouriteRestaurants = customerFavouriteRestaurantsRespository.findById(customerPhone).get();

        customerFavouriteRestaurants.setListOfRestaurantIds(listOfFavs);

        customerFavouriteRestaurantsRespository.save(customerFavouriteRestaurants);
        return listOfFavs;

    }
}
