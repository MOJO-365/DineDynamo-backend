package com.dinedynamo.repositories.restaurant_repositories;


import com.dinedynamo.collections.restaurant_collections.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends MongoRepository<AppUser, String> {


    List<AppUser> findByRestaurantId(String restaurantId);

    List<AppUser> deleteByRestaurantId(String restaurantId);


    Optional<AppUser> findByUserEmail(String userEmail);
}
