package com.dinedynamo.repositories.menu_repositories;

import com.dinedynamo.collections.menu_collections.Category;
import com.dinedynamo.collections.menu_collections.MenuItem;
import com.dinedynamo.collections.menu_collections.ParentType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {

    void deleteByRestaurantId(String restaurantId);

    @Query("{ 'restaurantId': ?0 }")
    List<Category> findByRestaurantId(String restaurantId);
}
