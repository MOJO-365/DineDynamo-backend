package com.dinedynamo.services.discounts_offers_services;


import com.dinedynamo.collections.discounts_offers.BogoOffer;
import com.dinedynamo.collections.discounts_offers.OfferType;
import com.dinedynamo.repositories.discounts_offers_repositories.BogoOfferRepository;
import com.dinedynamo.services.restaurant_services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BogoOfferService {

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    BogoOfferRepository bogoOfferRepository;

    public boolean isRequestValid(BogoOffer bogoOffer){

        if(bogoOffer.getOfferName()!=null || bogoOffer.getItems()!=null || bogoOffer.getEndingDate()!= null || bogoOffer.getRestaurantId() != null
                || bogoOffer.getStartingDate() != null || bogoOffer.getBuyQty() != null || bogoOffer.getGetQty()!=null
        ){

            if(restaurantService.isRestaurantPresentinDb(bogoOffer.getRestaurantId())){
                return true;
            }
            else{
                System.out.println("RESTAURNT-ID NOT FOUND IN DB");
                return false;
            }
        }

        return false;
    }


    public BogoOffer save(BogoOffer bogoOffer){

        bogoOffer.setOfferType(OfferType.BOGO);
        bogoOfferRepository.save(bogoOffer);
        return bogoOffer;

    }
}
