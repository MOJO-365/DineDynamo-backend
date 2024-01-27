package com.dinedynamo.repositories;


import com.dinedynamo.collections.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MenuRepository extends MongoRepository<Menu, String>
{
    Optional<Menu> findByRestaurantId(String restaurantId);
}
