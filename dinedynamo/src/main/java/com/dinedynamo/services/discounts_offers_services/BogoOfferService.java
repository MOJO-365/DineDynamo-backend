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
    BogoOfferRepository bogoOfferRepository;

    public BogoOffer save(BogoOffer bogoOffer){

        bogoOffer.setOfferType(OfferType.BOGO);
        bogoOfferRepository.save(bogoOffer);
        return bogoOffer;

    }
}
