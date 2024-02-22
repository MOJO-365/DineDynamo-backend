package com.dinedynamo.controllers.discounts_offers_controllers;

import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.Restaurant;
import com.dinedynamo.collections.discounts_offers.PercentageDiscount;
import com.dinedynamo.dto.discount_offers_dtos.EditPercentageDiscountDTO;
import com.dinedynamo.repositories.discounts_offers_repositories.PercentageDiscountRepository;
import com.dinedynamo.services.RestaurantService;
import com.dinedynamo.services.discounts_offers_services.PercentageDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
public class PercentageDiscountController
{
    @Autowired
    PercentageDiscountService percentageDiscountService;

    @Autowired
    PercentageDiscountRepository percentageDiscountRepository;

    @Autowired
    RestaurantService restaurantService;


    @PostMapping("/dinedynamo/restaurant/offers/create-percentage-discount")
    ResponseEntity<ApiResponse> createPercentageOffer(@RequestBody PercentageDiscount percentageDiscount){
        percentageDiscount = percentageDiscountService.save(percentageDiscount);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",percentageDiscount),HttpStatus.OK);

    }


    @DeleteMapping("/dinedynamo/restaurant/offers/delete-percentage-discount")
    ResponseEntity<ApiResponse> deletePercentageOffer(@RequestBody PercentageDiscount percentageDiscount){

        if(percentageDiscount.getDiscountOfferId() == null){
            System.out.println("DISCOUNT/OFFER ID NOT FOUND IN REQUEST BODY");
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_ACCEPTABLE,"success",null),HttpStatus.OK);

        }
        else{
            percentageDiscountRepository.delete(percentageDiscount);
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",percentageDiscount),HttpStatus.OK);
        }
    }

    @PutMapping("/dinedynamo/restaurant/offers/edit-percentage-discount")
    ResponseEntity<ApiResponse> editPercentageOffer(@RequestBody EditPercentageDiscountDTO editDiscountOfferDTO){

            PercentageDiscount newPercentageDiscount = (PercentageDiscount) editDiscountOfferDTO.getPercentageDiscount();

            newPercentageDiscount.setDiscountOfferId(editDiscountOfferDTO.getDiscountOfferId());

            percentageDiscountService.save(newPercentageDiscount);
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",newPercentageDiscount),HttpStatus.OK);


    }

    @PostMapping("/dinedynamo/restaurant/offers/get-percentage-discount-by-id")
    ResponseEntity<ApiResponse> getPercentageOfferById(@RequestParam String discountOfferId){

        PercentageDiscount percentageDiscount = percentageDiscountRepository.findById(discountOfferId).orElse(null);

        if(percentageDiscount == null){
            System.out.println("SUCH PERCENTAGE DISCOUNT NOT FOUND IN DB");
        }

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",percentageDiscount),HttpStatus.OK);

    }

    @PostMapping("/dinedynamo/restaurant/offers/get-all-percentage-discounts")
    ResponseEntity<ApiResponse> getAllPercentageOffers(@RequestBody Restaurant restaurant){

        if(!restaurantService.isRestaurantPresentinDb(restaurant.getRestaurantId())){
            System.out.println("RESTAURANT-ID NOT FOUND IN DB");
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",null),HttpStatus.OK);

        }

        List<PercentageDiscount> percentageDiscountList = percentageDiscountRepository.findByRestaurantId(restaurant.getRestaurantId()).orElse(new ArrayList<>());

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",percentageDiscountList),HttpStatus.OK);

    }

}
