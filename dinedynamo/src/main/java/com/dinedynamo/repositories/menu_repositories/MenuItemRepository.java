package com.dinedynamo.repositories.menu_repositories;

import com.dinedynamo.collections.menu_collections.MenuItem;
import com.dinedynamo.collections.menu_collections.ParentType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MenuItemRepository extends MongoRepository<MenuItem, String> {

    void deleteByRestaurantId(String restaurantId);


    @Query("{ 'restaurantId': ?0, 'parentId': ?1, 'parentType': ?2 }")
    List<MenuItem> findByRestaurantIdAndParentIdAndParentType(String restaurantId, String parentId, ParentType parentType);


    List<MenuItem> deleteByParentId(String parentId);

    List<MenuItem> findByRestaurantId(String restaurantId);
}
