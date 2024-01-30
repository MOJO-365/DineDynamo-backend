package com.dinedynamo.repositories;

import com.dinedynamo.collections.DeliveryOrder;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeliveryOrderRepository extends MongoRepository<DeliveryOrder,String>{
}
