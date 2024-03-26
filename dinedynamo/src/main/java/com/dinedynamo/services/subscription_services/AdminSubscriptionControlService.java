package com.dinedynamo.services.subscription_services;


import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.collections.subscriptions_collections.RestaurantSubscription;
import com.dinedynamo.collections.subscriptions_collections.RestaurantSubscriptionStatus;
import com.dinedynamo.collections.subscriptions_collections.SubscriptionPlan;
import com.dinedynamo.dto.subscription_dtos.ViewRestaurantSubscriptionDTO;
import com.dinedynamo.repositories.restaurant_repositories.RestaurantRepository;
import com.dinedynamo.repositories.subscription_repositories.RestaurantSubscriptionRepository;
import com.dinedynamo.repositories.subscription_repositories.SubscriptionPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminSubscriptionControlService {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    RestaurantSubscriptionRepository restaurantSubscriptionRepository;

    @Autowired
    SubscriptionPlanRepository subscriptionPlanRepository;

    public List<ViewRestaurantSubscriptionDTO> viewAllRestaurantSubscriptions(){

        List<ViewRestaurantSubscriptionDTO> allRestaurantSubscriptionList = new ArrayList<>();

        List<RestaurantSubscription> restaurantSubscriptionList = restaurantSubscriptionRepository.findAll();

        for(RestaurantSubscription restaurantSubscription: restaurantSubscriptionList){

            ViewRestaurantSubscriptionDTO viewRestaurantSubscriptionDTO = new ViewRestaurantSubscriptionDTO();

            Restaurant restaurant = restaurantRepository.findById(restaurantSubscription.getRestaurantId()).orElse(null);

            SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(restaurantSubscription.getSubscriptionPlanId()).orElse(null);

            viewRestaurantSubscriptionDTO.setRestaurant(restaurant);
            viewRestaurantSubscriptionDTO.setSubscriptionPlan(subscriptionPlan);
            viewRestaurantSubscriptionDTO.setRestaurantSubscription(restaurantSubscription);

            allRestaurantSubscriptionList.add(viewRestaurantSubscriptionDTO);

        }

        return allRestaurantSubscriptionList;
    }


    public List<ViewRestaurantSubscriptionDTO> viewAllActiveRestaurantSubscriptions(){


        List<ViewRestaurantSubscriptionDTO> allActiveRestaurantSubscriptionList = new ArrayList<>();

        List<RestaurantSubscription> restaurantSubscriptionList = restaurantSubscriptionRepository.findByRestaurantSubscriptionStatus(RestaurantSubscriptionStatus.ACTIVE);

        for(RestaurantSubscription restaurantSubscription: restaurantSubscriptionList){

            ViewRestaurantSubscriptionDTO viewRestaurantSubscriptionDTO = new ViewRestaurantSubscriptionDTO();

            Restaurant restaurant = restaurantRepository.findById(restaurantSubscription.getRestaurantId()).orElse(null);

            SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(restaurantSubscription.getSubscriptionPlanId()).orElse(null);

            viewRestaurantSubscriptionDTO.setRestaurant(restaurant);
            viewRestaurantSubscriptionDTO.setSubscriptionPlan(subscriptionPlan);
            viewRestaurantSubscriptionDTO.setRestaurantSubscription(restaurantSubscription);

            allActiveRestaurantSubscriptionList.add(viewRestaurantSubscriptionDTO);

        }

        return allActiveRestaurantSubscriptionList;

    }

    public List<ViewRestaurantSubscriptionDTO> viewAllInactiveRestaurantSubscriptions(){


        List<ViewRestaurantSubscriptionDTO> allInactiveRestaurantSubscriptionList = new ArrayList<>();

        List<RestaurantSubscription> restaurantSubscriptionList = restaurantSubscriptionRepository.findByRestaurantSubscriptionStatus(RestaurantSubscriptionStatus.INACTIVE);

        for(RestaurantSubscription restaurantSubscription: restaurantSubscriptionList){

            ViewRestaurantSubscriptionDTO viewRestaurantSubscriptionDTO = new ViewRestaurantSubscriptionDTO();

            Restaurant restaurant = restaurantRepository.findById(restaurantSubscription.getRestaurantId()).orElse(null);

            SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(restaurantSubscription.getSubscriptionPlanId()).orElse(null);

            viewRestaurantSubscriptionDTO.setRestaurant(restaurant);
            viewRestaurantSubscriptionDTO.setSubscriptionPlan(subscriptionPlan);
            viewRestaurantSubscriptionDTO.setRestaurantSubscription(restaurantSubscription);

            allInactiveRestaurantSubscriptionList.add(viewRestaurantSubscriptionDTO);

        }

        return allInactiveRestaurantSubscriptionList;

    }


}
