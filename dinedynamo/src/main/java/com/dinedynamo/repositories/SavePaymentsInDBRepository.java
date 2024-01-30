package com.dinedynamo.repositories;

import com.dinedynamo.collections.SuccessfullPayment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SavePaymentsInDBRepository extends MongoRepository<SuccessfullPayment, String>
{
}
