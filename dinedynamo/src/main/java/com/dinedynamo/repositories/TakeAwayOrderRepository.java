package com.dinedynamo.repositories;

import com.dinedynamo.collections.TakeAway;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TakeAwayOrderRepository extends MongoRepository<TakeAway,String> {
}
