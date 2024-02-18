package com.dinedynamo.repositories.menu_repositories;

import com.dinedynamo.collections.menu_collections.SubCategory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubCategoryRepository extends MongoRepository<SubCategory, String>
{

    void deleteByRestaurantId(String restaurantId);
}
