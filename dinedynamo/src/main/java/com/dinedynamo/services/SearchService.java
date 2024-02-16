package com.dinedynamo.services;


import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.stereotype.Service;
import com.dinedynamo.collections.Menu;
import com.dinedynamo.collections.Restaurant;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.dinedynamo.repositories.RestaurantRepository;

@Service
public class SearchService {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    MongoClient mongoClient;

    @Autowired
    MongoConverter mongoConverter;

    /**
     * Searches the expression in the collection - Menu and creats list of matched menus
     * Now from the matched menus list, corresponding restaurant ids are fetched and List of that restaurants is created
     */
    public List<Restaurant> searchInMenuOfRestaurantsAndReturnRestaurantList(String expression){

        List<Menu> matchedMenus = new ArrayList<>();
        List<Restaurant> matchedRestaurants = new ArrayList<>();
        MongoDatabase database = mongoClient.getDatabase("cluster0");
        MongoCollection<Document> collection = database.getCollection("menu");
        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$search",
                new Document("index", "search-menu")
                        .append("text",
                                new Document("query", expression)
                                        .append("path",
                                                new Document("wildcard", "*"))))));


        result.forEach(doc -> matchedMenus.add(mongoConverter.read(Menu.class,doc)));

        for(Menu menu: matchedMenus){

            String restaurantId = menu.getRestaurantId();

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
