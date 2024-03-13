package com.dinedynamo.repositories.inventory_repositories;

import com.dinedynamo.collections.inventory_management.RawMaterial;
import com.dinedynamo.collections.inventory_management.SupplierDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SupplierDetailsRepository extends MongoRepository<SupplierDetails, String> {

    List<SupplierDetails> deleteByRestaurantId(String restaurantId);

    //List<SupplierDetails> deleteByRawMaterialId(String rawMaterialId);

    List<SupplierDetails> findByRestaurantId(String restaurantId);

    //List<SupplierDetails> findByRawMaterialId(String rawMaterialId);


    @Query("{'restaurantId': ?0, 'supplierName': {$regex : ?1, $options: 'i'}}")
    List<SupplierDetails> findByRestaurantIdAndSupplierName(String restaurantId, String supplierName);
}
