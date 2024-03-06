package com.dinedynamo.repositories.inventory_repositories;

import com.dinedynamo.collections.inventory_management.WastageLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface WastageLogRepository extends MongoRepository<WastageLog, String> {

    List<WastageLog> deleteByRestaurantId(String restaurantId);

    List<WastageLog> deleteByRawMaterialId(String rawMaterialId);

    List<WastageLog> findByRawMaterialId(String rawMaterialId);

    List<WastageLog> findByRestaurantId(String restaurantId);
}
