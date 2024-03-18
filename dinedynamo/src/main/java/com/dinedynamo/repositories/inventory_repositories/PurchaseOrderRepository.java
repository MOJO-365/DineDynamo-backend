package com.dinedynamo.repositories.inventory_repositories;


import com.dinedynamo.collections.inventory_management.PurchaseOrder;
import com.dinedynamo.collections.inventory_management.PurchaseOrderStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderRepository extends MongoRepository<PurchaseOrder, String> {

    List<PurchaseOrder> findByRawMaterialId(String rawMaterialId);

    List<PurchaseOrder> findByRestaurantId(String rawMaterialId);

    List<PurchaseOrder> findByRestaurantIdAndStatus(String restaurantId, PurchaseOrderStatus status);



}
