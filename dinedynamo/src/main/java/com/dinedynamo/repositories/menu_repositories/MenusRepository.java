package com.dinedynamo.repositories.menu_repositories;

import com.dinedynamo.collections.menu_collections.Menus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MenusRepository extends MongoRepository<Menus, String> {

    void deleteByRestaurantId(String restaurantId);
}
