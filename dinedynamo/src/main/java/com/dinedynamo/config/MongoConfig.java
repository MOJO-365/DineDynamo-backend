package com.dinedynamo.config;
import com.dinedynamo.collections.restaurant_collections.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.IndexOptions;

@Configuration
public class MongoConfig {

    @Autowired
    private   MongoTemplate mongoTemplate;

//    @Bean
//    public MongoTemplate mongoTemplate() {
//        IndexOperations indexOps = mongoTemplate.indexOps(AppUser.class);
//        indexOps.ensureIndex(new Index().on("userEmail",IndexOptions.unique()));
//        return mongoTemplate;
//    }
}
