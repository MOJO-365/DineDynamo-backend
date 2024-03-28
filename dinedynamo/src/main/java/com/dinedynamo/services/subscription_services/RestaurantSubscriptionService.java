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

    public SubscriptionResponseDTO save(SubscriptionRequestDTO subscriptionRequestDTO){

        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(subscriptionRequestDTO.getSubscriptionPlanId()).orElse(null);

        if(subscriptionPlan == null){
            throw new RuntimeException("Subscription Plan id not found in db");
        }
        else{

            SubscriptionResponseDTO subscriptionResponseDTO = new SubscriptionResponseDTO();

            if(subscriptionRequestDTO.isPaymentDone()){

                RestaurantSubscription restaurantSubscription = new RestaurantSubscription();
                restaurantSubscription.setSubscriptionPlanId(subscriptionRequestDTO.getSubscriptionPlanId());
                restaurantSubscription.setRestaurantId(subscriptionRequestDTO.getRestaurantId());
                restaurantSubscription.setStartDate(subscriptionRequestDTO.getStartDate());
                restaurantSubscription.setEndDate(subscriptionRequestDTO.getEndDate());
                restaurantSubscription.setRestaurantSubscriptionStatus(getCurrentRestaurantSubscriptionStatus(subscriptionRequestDTO.getStartDate(), subscriptionRequestDTO.getEndDate()));

                restaurantSubscriptionRepository.save(restaurantSubscription);

                subscriptionResponseDTO.setRestaurantSubscription(restaurantSubscription);
                subscriptionResponseDTO.setSavedToDb(true);
                subscriptionResponseDTO.setPaymentDone(true);


            }
            else{
                System.out.println("PAYMENT FOR SUBSCRIPTION NOT DONE");
                subscriptionResponseDTO.setRestaurantSubscription(null);
                subscriptionResponseDTO.setSavedToDb(false);
                subscriptionResponseDTO.setPaymentDone(false);

            }

            return subscriptionResponseDTO;
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


    //@Scheduled(fixedRate =  24 * 60 * 60 * 1000)
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
