package com.dinedynamo.repositories;


import com.dinedynamo.collections.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MenuRepository extends MongoRepository<Menu, String>
{
    Optional<Menu> findByRestaurantId(String restaurantId);


    @Query("{'listOfMenuItem': {$regex: ?0, $options: 'i'}}")
    List<Menu> findByListOfMenuItemRegexIgnoreCase(String searchText);


    @Query("{'listOfMenuItem': { $text: { $search: ?0 } } }")
    List<Menu> findByTextSearch(String searchText);

    List<Menu> findByListOfMenuItemContainingIgnoreCase(String searchText);
}
