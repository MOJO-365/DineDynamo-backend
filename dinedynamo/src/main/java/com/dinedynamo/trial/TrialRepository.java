package com.dinedynamo.trial;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TrialRepository extends MongoRepository<TrialCollection, String>
{
}
