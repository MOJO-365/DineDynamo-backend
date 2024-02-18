package com.dinedynamo.repositories.menu_repositories;

import com.dinedynamo.collections.menu_collections.SubSubCategory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubSubCategoryRepository extends MongoRepository<SubSubCategory, String> {
    void deleteByRestaurantId(String restaurantId);

}
