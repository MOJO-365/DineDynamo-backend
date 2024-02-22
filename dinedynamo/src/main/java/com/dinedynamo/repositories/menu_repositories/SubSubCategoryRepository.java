package com.dinedynamo.repositories.menu_repositories;

import com.dinedynamo.collections.menu_collections.SubCategory;
import com.dinedynamo.collections.menu_collections.SubSubCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface SubSubCategoryRepository extends MongoRepository<SubSubCategory, String> {
    void deleteByRestaurantId(String restaurantId);

    @Query("{ 'restaurantId': ?0, 'subCategoryId': ?1}")
    List<SubSubCategory> findByRestaurantIdAndSubCategoryId(String restaurantId, String subCategoryId);


    List<SubSubCategory> deleteBySubCategoryId(String subCategoryId);
}
