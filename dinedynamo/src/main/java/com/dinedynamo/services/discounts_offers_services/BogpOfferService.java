package com.dinedynamo.services.discounts_offers_services;

import com.dinedynamo.collections.discounts_offers.BogpOffer;
import com.dinedynamo.collections.discounts_offers.OfferType;
import com.dinedynamo.repositories.discounts_offers_repositories.BogpOfferRepository;
import com.dinedynamo.services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BogpOfferService
{

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    BogpOfferRepository bogpOfferRepository;

    public boolean isRequestValid(BogpOffer bogpOffer){

        if(bogpOffer.getOfferName()!=null || bogpOffer.getItems()!=null || bogpOffer.getEndingDate()!= null || bogpOffer.getRestaurantId() != null
                || bogpOffer.getStartingDate() != null || bogpOffer.getPercentage() != null || bogpOffer.getMinQty()!=null
        ){

            if(restaurantService.isRestaurantPresentinDb(bogpOffer.getRestaurantId())){
                return true;
            }
            else{
                System.out.println("RESTAURNT-ID NOT FOUND IN DB");
                return false;
            }
        }

        return false;
    }


    public BogpOffer save(BogpOffer bogpOffer){

        bogpOffer.setOfferType(OfferType.BOGP);
        bogpOfferRepository.save(bogpOffer);
        return bogpOffer;

    }
}
