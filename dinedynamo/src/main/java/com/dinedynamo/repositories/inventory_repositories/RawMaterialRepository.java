package com.dinedynamo.repositories.inventory_repositories;


import com.dinedynamo.collections.inventory_management.RawMaterial;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RawMaterialRepository extends MongoRepository<RawMaterial, String>
{
    List<RawMaterial> findByRestaurantId(String restaurantId);
}
