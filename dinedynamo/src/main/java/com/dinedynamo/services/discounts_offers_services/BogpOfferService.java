package com.dinedynamo.services.discounts_offers_services;

import com.dinedynamo.collections.discounts_offers.BogpOffer;
import com.dinedynamo.collections.discounts_offers.OfferType;
import com.dinedynamo.repositories.discounts_offers_repositories.BogpOfferRepository;
import com.dinedynamo.services.restaurant_services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BogpOfferService
{

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    BogpOfferRepository bogpOfferRepository;



    public BogpOffer save(BogpOffer bogpOffer){

        bogpOffer.setOfferType(OfferType.BOGP);
        bogpOfferRepository.save(bogpOffer);
        return bogpOffer;

    }
}
