package com.dinedynamo.repositories.inventory_repositories;

import com.dinedynamo.collections.inventory_management.WastageLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WastageLogRepository extends MongoRepository<WastageLog, String> {
}
