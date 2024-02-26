package com.dinedynamo.config.jwt_config;


import com.dinedynamo.collections.authentication_collections.Customer;
import com.dinedynamo.collections.authentication_collections.Restaurant;

import com.dinedynamo.repositories.CustomerRepository;
import com.dinedynamo.repositories.RestaurantRepository;

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
    private RestaurantRepository restaurantRepository;

    @Autowired
    private CustomerRepository customerRepository;

    UserDetails userDetails;

    @Setter
    @Getter
    String userRole;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {

        String userRole = getUserRole();


        if (userRole.toUpperCase().equals("RESTAURANT")) {
            Restaurant restaurant = restaurantRepository.findByRestaurantEmail(userEmail).orElse(null);

            if (restaurant == null) {
                System.out.println("THIS RESTAURANT DOES NOT EXIST IN DB");
                return null;
            }

            return new org.springframework.security.core.userdetails.User(
                    restaurant.getRestaurantEmail(),
                    restaurant.getRestaurantPassword(),
                    AuthorityUtils.createAuthorityList(userRole)
            );

        } else if (userRole.toUpperCase().equals("CUSTOMER")) {
            Customer customer = customerRepository.findByCustomerEmail(userEmail).orElse(null);


            if (customer == null) {
                System.out.println("THIS CUSTOMER DOES NOT EXIST IN DB");
                return null;
            }

            return new org.springframework.security.core.userdetails.User(
                    customer.getCustomerEmail(),
                    customer.getCustomerPassword(),
                    AuthorityUtils.createAuthorityList(userRole)
            );

        }


        System.out.println("USER DATA NOT FOUND IN DB, INAPPROPRIATE CREDENTIALS");
        return null;
    }

//        User user = userRepository.findByUserEmail(userEmail).get(); // Implement this method in your UserRepository
//
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found with email: " + userEmail);
//        }
//        return new org.springframework.security.core.userdetails.User(
//                user.getUserEmail(),
//                user.getUserPassword(),
//                AuthorityUtils.createAuthorityList(user.getUserRole())
//        );
    }
//        Optional<User> user = userRepository.findByUserEmail(userEmail);
//        return user.map(UserDetailsImpl::new)
//                .orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND " + userEmail));





