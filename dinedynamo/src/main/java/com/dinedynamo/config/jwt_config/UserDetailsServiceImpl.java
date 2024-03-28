package com.dinedynamo.config.jwt_config;


import com.dinedynamo.collections.customer_collections.Customer;
import com.dinedynamo.collections.restaurant_collections.AppUser;
import com.dinedynamo.collections.restaurant_collections.Restaurant;

import com.dinedynamo.repositories.customer_repositories.CustomerRepository;
import com.dinedynamo.repositories.restaurant_repositories.AppUserRepository;
import com.dinedynamo.repositories.restaurant_repositories.RestaurantRepository;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class UserDetailsServiceImpl  implements UserDetailsService {


    @Autowired
    AppUserRepository appUserRepository;



    @Setter
    @Getter
    String userRole;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {

        System.out.println("IN UserDetailsServiceImpl: "+appUserRepository.findByUserEmail(userEmail));
        return appUserRepository.findByUserEmail(userEmail).orElseThrow(()-> new UsernameNotFoundException("User not found"));


        //        String userRole = getUserRole();
//        AppUser appUser = appUserRepository.findByUserEmail(userEmail).orElse(null);
//
//        //Restaurant restaurant = restaurantRepository.findByRestaurantEmail(userEmail).orElse(null);
//
//        if(appUser == null){
//            throw new RuntimeException("App user does not exist in DB");
//        }
//
//        return new org.springframework.security.core.userdetails.User(
//                appUser.getUserEmail(),
//                appUser.getUserPassword(),
//                AuthorityUtils.createAuthorityList(userRole)
//        );

    }


    }



