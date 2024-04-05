package com.dinedynamo.services.subscription_services;


import com.dinedynamo.collections.subscriptions_collections.RestaurantSubscription;
import com.dinedynamo.collections.subscriptions_collections.RestaurantSubscriptionStatus;
import com.dinedynamo.collections.subscriptions_collections.SubscriptionPlan;
import com.dinedynamo.dto.subscription_dtos.SubscriptionRequestDTO;
import com.dinedynamo.dto.subscription_dtos.SubscriptionResponseDTO;
import com.dinedynamo.repositories.subscription_repositories.RestaurantSubscriptionRepository;
import com.dinedynamo.repositories.subscription_repositories.SubscriptionPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RestaurantSubscriptionService {


    @Autowired
    RestaurantSubscriptionRepository restaurantSubscriptionRepository;

    @Autowired
    SubscriptionPlanRepository subscriptionPlanRepository;

    public RestaurantSubscription save(SubscriptionRequestDTO subscriptionRequestDTO){

        SubscriptionPlan requestedSubscriptionPlan = subscriptionPlanRepository.findById(subscriptionRequestDTO.getSubscriptionPlanId()).orElse(null);

        RestaurantSubscription existingRestaurantSubscription = restaurantSubscriptionRepository.findByRestaurantId(subscriptionRequestDTO.getRestaurantId()).orElse(null);

        if(requestedSubscriptionPlan == null){
            throw new RuntimeException("Subscription Plan id not found in db");
        }

        //If the restaurant takes subscription for the first time
        if(existingRestaurantSubscription == null){

            RestaurantSubscription restaurantSubscription = new RestaurantSubscription();
            restaurantSubscription.setSubscriptionPlanId(subscriptionRequestDTO.getSubscriptionPlanId());
            restaurantSubscription.setRestaurantId(subscriptionRequestDTO.getRestaurantId());
            restaurantSubscription.setStartDate(LocalDate.now());

            System.out.println("-------------> After setter mtd: "+restaurantSubscription.getStartDate());
            restaurantSubscription.setEndDate(LocalDate.now().plusMonths((long) requestedSubscriptionPlan.getNoOfMonths()));
            restaurantSubscription.setRestaurantSubscriptionStatus(getCurrentRestaurantSubscriptionStatus(restaurantSubscription.getStartDate(), restaurantSubscription.getEndDate()));

            restaurantSubscription.setRenewed(false);
            restaurantSubscriptionRepository.save(restaurantSubscription);
            return restaurantSubscription;
        }

        else{

            existingRestaurantSubscription.setSubscriptionPlanId(requestedSubscriptionPlan.getSubscriptionPlanId());


            existingRestaurantSubscription.setEndDate(existingRestaurantSubscription.getEndDate().
                    plusMonths((long)requestedSubscriptionPlan.
                            getNoOfMonths()));

            existingRestaurantSubscription.setRestaurantSubscriptionStatus(getCurrentRestaurantSubscriptionStatus(existingRestaurantSubscription.getStartDate(), existingRestaurantSubscription.getEndDate()));

            existingRestaurantSubscription.setRenewed(true);
            restaurantSubscriptionRepository.save(existingRestaurantSubscription);

            return existingRestaurantSubscription;


        }
    }


    //For setting/updating the status of restaurant subscription
    public RestaurantSubscriptionStatus getCurrentRestaurantSubscriptionStatus(LocalDate startDate, LocalDate endDate){

        if((LocalDate.now().isBefore(endDate) || LocalDate.now().isEqual(endDate)) && (LocalDate.now().isAfter(startDate) || LocalDate.now().isEqual(startDate))){
            return RestaurantSubscriptionStatus.ACTIVE;
        }
        else{
            return RestaurantSubscriptionStatus.INACTIVE;
        }

    }


    @Scheduled(fixedRate =  24 * 60 * 60 * 1000)
    public void updateSubscriptionStatusInDatabase(){

        List<RestaurantSubscription> restaurantSubscriptionList = restaurantSubscriptionRepository.findAll();

        for(RestaurantSubscription restaurantSubscription: restaurantSubscriptionList){

            LocalDate currentDate = LocalDate.now();
            if((currentDate.isBefore(restaurantSubscription.getEndDate()) || currentDate.isEqual(restaurantSubscription.getEndDate())) && (currentDate.isAfter(restaurantSubscription.getStartDate()) || currentDate.isEqual(restaurantSubscription.getStartDate()))){
                restaurantSubscription.setRestaurantSubscriptionStatus(RestaurantSubscriptionStatus.ACTIVE);
            }
            else{
                restaurantSubscription.setRestaurantSubscriptionStatus(RestaurantSubscriptionStatus.INACTIVE);
            }

            restaurantSubscriptionRepository.save(restaurantSubscription);
        }

        System.out.println("UPDATED SUBSCRIPTION STATUS..");
    }
}
