package com.dinedynamo.services.restaurant_services;


import com.dinedynamo.collections.restaurant_collections.AppUser;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.repositories.restaurant_repositories.AppUserRepository;
import com.dinedynamo.repositories.restaurant_repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService {

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public AppUser save(AppUser appUser){

        String password = appUser.getUserPassword();
        AppUser existingAppUser = appUserRepository.findByUserEmail(appUser.getUserEmail()).orElse(null);

        if(existingAppUser!=null){

            System.out.println("NO DUPLICATE EMAILS ALLOWED IN APP USER COLLECTION");
            return null;
        }


        else{
            Restaurant restaurant = restaurantRepository.findById(appUser.getRestaurantId()).orElse(null);

            if(restaurant == null){
                System.out.println("RESTAURANT NOT FOUND IN DB");
                throw new RuntimeException("Restaurant not found in database");
            }

            else{
                System.out.println("else block...........");
                appUser.setUserPassword(passwordEncoder.encode(password));
                appUserRepository.save(appUser);
                return appUser;
            }
        }
    }

    public AppUser editAppuser(String userId, AppUser appUser){

        Restaurant restaurant = restaurantRepository.findById(appUser.getRestaurantId()).orElse(null);

        if(restaurant == null){
            System.out.println("RESTAURANT NOT FOUND IN DB");
            throw new RuntimeException("Restaurant not found in database");
        }

        else{
            appUser.setUserId(userId);
            appUserRepository.save(appUser);

            return appUser;
        }
    }

    public List<AppUser> findAllUsersByRestaurantId(Restaurant restaurant){

        restaurant = restaurantRepository.findById(restaurant.getRestaurantId()).orElse(null);

        if(restaurant == null){
            System.out.println("RESTAURANT NOT FOUND IN DB");
            throw new RuntimeException("Restaurant not found in database");
        }

        else{
            List<AppUser> appUserList = appUserRepository.findByRestaurantId(restaurant.getRestaurantId());
            return appUserList;
        }
    }

    public List<AppUser> deleteAllUsersByRestaurantId(Restaurant restaurant){

        restaurant = restaurantRepository.findById(restaurant.getRestaurantId()).orElse(null);

        if(restaurant == null){
            System.out.println("RESTAURANT NOT FOUND IN DB");
            throw new RuntimeException("Restaurant not found in database");
        }

        else{
            List<AppUser> appUserList = appUserRepository.deleteByRestaurantId(restaurant.getRestaurantId());
            return appUserList;
        }
    }

    public AppUser updateRestaurant(Restaurant restaurant){
        restaurant = restaurantRepository.findByRestaurantEmail(restaurant.getRestaurantEmail()).orElse(null);

        AppUser appUser = appUserRepository.findByUserEmail(restaurant.getRestaurantEmail()).orElse(null);

        if(appUser == null){
            appUser = saveRestaurant(restaurant);
            return appUser;
        }
        else{

            appUser.setRestaurantId(restaurant.getRestaurantId());
            appUser.setUserEmail(restaurant.getRestaurantEmail());
            appUser.setUserPassword(restaurant.getRestaurantPassword());
            appUser.setUserType("RESTAURANT");
            appUserRepository.save(appUser);
            return appUser;
        }

    }


    public AppUser saveRestaurant(Restaurant restaurant){

        restaurant = restaurantRepository.findByRestaurantEmail(restaurant.getRestaurantEmail()).orElse(null);

        if(restaurant != null){
            AppUser appUser = new AppUser();
            appUser.setRestaurantId(restaurant.getRestaurantId());
            appUser.setUserEmail(restaurant.getRestaurantEmail());
            appUser.setUserPassword(restaurant.getRestaurantPassword());
            appUser.setUserType("RESTAURANT");
            appUserRepository.save(appUser);

            return appUser;
        }
        else{
            System.out.println("Restaurant not saved in 'restaurant' collection ");
            throw new RuntimeException("Restaurant not saved in 'restaurant' collection");
        }
    }
}
