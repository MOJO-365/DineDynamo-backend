package com.dinedynamo.repositories.inventory_repositories;

import com.dinedynamo.collections.inventory_management.RawMaterial;
import com.dinedynamo.collections.inventory_management.ReplenishmentLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplenishmentLogRepository extends MongoRepository<ReplenishmentLog, String> {

    List<ReplenishmentLog> deleteByRestaurantId(String restaurantId);

    List<ReplenishmentLog> deleteByRawMaterialId(String rawMaterialId);

    List<ReplenishmentLog> findByRawMaterialId(String rawMaterialId);

    List<ReplenishmentLog> findByRestaurantId(String restaurantId);
}
