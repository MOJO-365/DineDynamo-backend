package com.dinedynamo.services;


import com.dinedynamo.collections.BugQuery;
import com.dinedynamo.collections.Restaurant;
import com.dinedynamo.repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RaiseBugQueryService
{

    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    EmailService emailService;

    public boolean createAndSendQuery(BugQuery bugQuery){

        if(bugQuery.getBugDescription().equals("") || bugQuery.getBugDescription() == null ||
                bugQuery.getBugDescription().equals(" ")  || bugQuery.getRestaurantId() == null
        ){

            System.out.println("DESCRIPTION IS EMPTY OR RESTAURANT ID IS NOT PROPER");
            //throw new RuntimeException("No sufficient attributes in request body");
            return false;
        }

        Restaurant restaurant = restaurantRepository.findById(bugQuery.getRestaurantId()).orElse(null);


        if(restaurant == null){

            System.out.println("RESTAURANT-ID NOT PRESENT IN DATABASE");
            //throw new RuntimeException("Restaurant-id not found in database");
            return false;
        }


        boolean isSent = emailService.sendBugQueryMail(bugQuery.getBugQueryTitle(),restaurant.getRestaurantName() ,bugQuery.getBugDescription());

        if(!isSent){
            throw new RuntimeException("Cannot send email, something went wrong");
        }
        return true;
    }
}
