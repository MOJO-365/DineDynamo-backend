package com.dinedynamo.services;


import com.dinedynamo.collections.menu_collections.Menus;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.stereotype.Service;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.dinedynamo.repositories.restaurant_repositories.RestaurantRepository;

@Service
public class SearchService {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    MongoClient mongoClient;

    @Autowired
    MongoConverter mongoConverter;

    /**
     * Searches the expression in the collection - 'Menus' and creates list of matched menus
     * Now from the matched menus list, corresponding restaurant ids are fetched and List of that restaurants is created
     */
    public List<Restaurant> searchInMenusOfRestaurantsAndReturnRestaurantList(String expression){

        List<Menus> matchedMenus = new ArrayList<>();
        List<Restaurant> matchedRestaurants = new ArrayList<>();
        MongoDatabase database = mongoClient.getDatabase("cluster0");
        MongoCollection<Document> collection = database.getCollection("menus");
        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$search",
                new Document("index", "search-menus")
                        .append("text",
                                new Document("query", expression)
                                        .append("path",
                                                new Document("wildcard", "*"))))));


        result.forEach(doc -> matchedMenus.add(mongoConverter.read(Menus.class,doc)));

        for(Menus menus: matchedMenus){

            String restaurantId = menus.getRestaurantId();

            Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);

            if(restaurant!= null){

                if(!matchedRestaurants.contains(restaurant)){
                    matchedRestaurants.add(restaurant);
                }
            }
        }

        return matchedRestaurants;

    }
}
