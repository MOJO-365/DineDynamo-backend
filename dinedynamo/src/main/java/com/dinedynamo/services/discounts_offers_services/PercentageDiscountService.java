package com.dinedynamo.services.discounts_offers_services;

import com.dinedynamo.collections.discounts_offers.OfferType;
import com.dinedynamo.collections.discounts_offers.PercentageDiscount;
import com.dinedynamo.repositories.discounts_offers_repositories.PercentageDiscountRepository;
import com.dinedynamo.services.restaurant_services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PercentageDiscountService {

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    PercentageDiscountRepository percentageDiscountRepository;


    public PercentageDiscount save(PercentageDiscount percentageDiscount){

        percentageDiscount.setOfferType(OfferType.PERCENTAGE);
        percentageDiscountRepository.save(percentageDiscount);
        return percentageDiscount;

    }
}
