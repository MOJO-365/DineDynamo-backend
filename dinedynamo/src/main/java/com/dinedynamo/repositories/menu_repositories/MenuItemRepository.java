package com.dinedynamo.repositories.menu_repositories;

import com.dinedynamo.collections.menu_collections.MenuItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MenuItemRepository extends MongoRepository<MenuItem, String> {

    void deleteByRestaurantId(String restaurantId);
}
