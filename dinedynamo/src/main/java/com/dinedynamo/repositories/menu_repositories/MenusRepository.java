package com.dinedynamo.repositories.menu_repositories;

import com.dinedynamo.collections.menu_collections.Menus;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MenusRepository extends MongoRepository<Menus, String> {

    void deleteByRestaurantId(String restaurantId);

    Optional<Menus> findByRestaurantId(String restaurantId);
}
