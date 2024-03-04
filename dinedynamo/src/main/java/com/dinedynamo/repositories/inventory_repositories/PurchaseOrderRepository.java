package com.dinedynamo.repositories.inventory_repositories;


import com.dinedynamo.collections.inventory_management.PurchaseOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderRepository extends MongoRepository<PurchaseOrder, String> {
}
