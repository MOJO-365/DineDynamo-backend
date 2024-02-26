package com.dinedynamo.repositories.order_repositories;

import com.dinedynamo.collections.customer_collections.CustomerFavouriteRestaurants;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerFavouriteRestaurantsRespository extends MongoRepository<CustomerFavouriteRestaurants, String> {
}
