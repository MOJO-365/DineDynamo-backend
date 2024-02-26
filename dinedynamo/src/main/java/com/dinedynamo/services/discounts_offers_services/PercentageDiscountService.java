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


    public boolean isRequestValid(PercentageDiscount percentageDiscount){

        if(percentageDiscount.getPercentage()!=null || percentageDiscount.getItems()!=null || percentageDiscount.getMaxValue()!= null || percentageDiscount.getRestaurantId() != null
        || percentageDiscount.getMinValue() != null || percentageDiscount.getMinQty() != null || percentageDiscount.getEndingDate()!=null || percentageDiscount.getStartingDate()!=null
        ){

            if(restaurantService.isRestaurantPresentinDb(percentageDiscount.getRestaurantId())){
                return true;
            }
            else{
                System.out.println("RESTAURNT-ID NOT FOUND IN DB");
                return false;
            }


        }
        return false;
    }

    public PercentageDiscount save(PercentageDiscount percentageDiscount){

        percentageDiscount.setOfferType(OfferType.PERCENTAGE);
        percentageDiscountRepository.save(percentageDiscount);
        return percentageDiscount;

    }
}
