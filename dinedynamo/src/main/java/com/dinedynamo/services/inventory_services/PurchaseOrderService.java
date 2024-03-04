package com.dinedynamo.services.inventory_services;

import com.dinedynamo.collections.inventory_management.PurchaseOrder;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.repositories.inventory_repositories.PurchaseOrderRepository;
import com.dinedynamo.repositories.restaurant_repositories.RestaurantRepository;
import com.dinedynamo.services.external_services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PurchaseOrderService {


    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    EmailService emailService;

    public PurchaseOrder save(PurchaseOrder purchaseOrder){

        Restaurant restaurant = restaurantRepository.findById(purchaseOrder.getRestaurantId()).orElse(null);

        if(restaurant == null){
            System.out.println("RESTAURANT-ID NOT IN DB");
            throw new RuntimeException("Restaurant not found in db");
        }

        else{
            purchaseOrder.setDateOfPurchaseRequest(LocalDate.now());
            purchaseOrderRepository.save(purchaseOrder);

            emailService.raisePurchaseOrderTicket(purchaseOrder, restaurant.getRestaurantName());
            return purchaseOrder;

        }
    }
}
