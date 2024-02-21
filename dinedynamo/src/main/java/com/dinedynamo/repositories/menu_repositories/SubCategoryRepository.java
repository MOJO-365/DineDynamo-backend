package com.dinedynamo.repositories.menu_repositories;

import com.dinedynamo.collections.menu_collections.SubCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface SubCategoryRepository extends MongoRepository<SubCategory, String>
{

    void deleteByRestaurantId(String restaurantId);

    @Query("{ 'restaurantId': ?0, 'categoryId': ?1}")
    List<SubCategory> findByRestaurantIdAndCategoryId(String restaurantId, String categoryId);


}
