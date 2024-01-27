package com.dinedynamo.services;


import com.dinedynamo.collections.Menu;
import com.dinedynamo.repositories.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MenuService
{
    @Autowired
    MenuRepository menuRepository;

    public Menu findById(String menuId){
        return menuRepository.findById(menuId).orElse(null);
    }


    public Menu findByRestaurantId(String restaurantId){
        return menuRepository.findByRestaurantId(restaurantId).orElse(null);
    }

}
