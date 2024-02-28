package com.dinedynamo.repositories.inventory_repositories;


import com.dinedynamo.collections.inventory_management.RawMaterial;
import com.dinedynamo.collections.inventory_management.RawMaterialStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RawMaterialRepository extends MongoRepository<RawMaterial, String>
{
    List<RawMaterial> findByRestaurantId(String restaurantId);

    List<RawMaterial> deleteByRestaurantId(String restaurantId);

    @Query("{'restaurantId': ?0, 'category': {$regex : ?1, $options: 'i'}}")
    List<RawMaterial> findByRestaurantIdAndCategory(String restaurantId, String category);

    @Query("{'restaurantId': ?0, 'name': {$regex : ?1, $options: 'i'}}")
    List<RawMaterial> findByRestaurantIdAndName(String restaurantId, String name);

    List<RawMaterial> findByRestaurantIdAndStatus(String restaurantId, RawMaterialStatus status);

    List<RawMaterial> findByRestaurantId(String restaurantId, Sort sort);

    List<RawMaterial> findByRestaurantIdAndExpirationDateBefore(String restaurantId, LocalDate currentDate);

}
