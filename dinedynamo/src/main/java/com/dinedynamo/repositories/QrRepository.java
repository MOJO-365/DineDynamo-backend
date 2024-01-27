package com.dinedynamo.repositories;


import com.dinedynamo.collections.QrOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface QrRepository extends MongoRepository<QrOrder, String> {
}
