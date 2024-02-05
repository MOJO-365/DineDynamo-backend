package com.dinedynamo.services;


import com.dinedynamo.collections.Restaurant;
import com.dinedynamo.collections.WaitingList;
import com.dinedynamo.repositories.RestaurantRepository;
import com.dinedynamo.repositories.WaitingListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WaitingListService {

    @Autowired
    WaitingListRepository waitingListRepository;

    @Autowired
    RestaurantRepository restaurantRepository;



    public boolean isWaitingListRequestValid(WaitingList waitingList){

        if(waitingList.getCustomerName() == null ||   waitingList.getCustomerName() == ""  ||
                waitingList.getCustomerPhone() == null || waitingList.getCustomerPhone() == "" ||
                waitingList.getGuestCount() == 0 ||
                waitingList.getRestaurantId() == null  || waitingList.getRestaurantId() == ""
        ){
            return false;
        }


        return true;
    }



    public boolean save(WaitingList waitingList){
        waitingList.setReservationDateAndTime(LocalDateTime.now());

        String restaurantId = waitingList.getRestaurantId();

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);


        if(restaurant == null){
            System.out.println("RESTAURANT-ID NOT FOUND IN DB");
            return false;
        }
        waitingListRepository.save(waitingList);
        System.out.println("DATA SAVED IN WAITING-LIST DB");
        return true;
    }



}
