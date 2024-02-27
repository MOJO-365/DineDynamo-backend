package com.dinedynamo.repositories.inventory_repositories;

import com.dinedynamo.collections.inventory_management.SupplierDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SupplierDetailsRepository extends MongoRepository<SupplierDetails, String> {
}
