package com.dinedynamo.repositories.menu_repositories;

import com.dinedynamo.collections.menu_collections.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {

    void deleteByRestaurantId(String restaurantId);
}
