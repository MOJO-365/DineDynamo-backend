package com.dinedynamo.controllers.discounts_offers_controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.authentication_collections.Restaurant;
import com.dinedynamo.collections.discounts_offers.BogoOffer;

import com.dinedynamo.dto.discount_offers_dtos.EditBogoOfferDTO;
import com.dinedynamo.repositories.discounts_offers_repositories.BogoOfferRepository;
import com.dinedynamo.services.RestaurantService;
import com.dinedynamo.services.discounts_offers_services.BogoOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
public class BogoOfferController {


    @Autowired
    BogoOfferRepository bogoOfferRepository;

    @Autowired
    BogoOfferService bogoOfferService;

    @Autowired
    RestaurantService restaurantService;

    @PostMapping("/dinedynamo/restaurant/offers/create-bogo-offer")
    ResponseEntity<ApiResponse> createBogoOffer(@RequestBody BogoOffer bogoOffer){

        bogoOffer = bogoOfferService.save(bogoOffer);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",bogoOffer),HttpStatus.OK);

    }

    @DeleteMapping("/dinedynamo/restaurant/offers/delete-bogo-offer")
    ResponseEntity<ApiResponse> deleteBogoOffer(@RequestBody BogoOffer bogoOffer){

        if(bogoOffer.getDiscountOfferId() == null){
            System.out.println("DISCOUNT/OFFER ID NOT FOUND IN REQUEST BODY");
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_ACCEPTABLE,"success",null),HttpStatus.OK);

        }
        else{
            bogoOfferRepository.delete(bogoOffer);
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_ACCEPTABLE,"success",bogoOffer),HttpStatus.OK);
        }
    }

    @PutMapping("/dinedynamo/restaurant/offers/edit-bogo-offer")
    ResponseEntity<ApiResponse> editBogoOffer(@RequestBody EditBogoOfferDTO editBogoOfferDTO){


            BogoOffer newBogoOffer = editBogoOfferDTO.getBogoOffer();

            newBogoOffer.setDiscountOfferId(editBogoOfferDTO.getDiscountOfferId());

            newBogoOffer = bogoOfferService.save(newBogoOffer);
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",newBogoOffer),HttpStatus.OK);


    }

    @PostMapping("/dinedynamo/restaurant/offers/get-bogo-offer-by-id")
    ResponseEntity<ApiResponse> getBogoOfferById(@RequestParam String discountOfferId){

        BogoOffer bogoOffer = bogoOfferRepository.findById(discountOfferId).orElse(null);

        if(bogoOffer == null){
            System.out.println("SUCH BOGO OFFER NOT FOUND IN DB");
        }

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",bogoOffer),HttpStatus.OK);

    }

    @PostMapping("/dinedynamo/restaurant/offers/get-all-bogo-offers")
    ResponseEntity<ApiResponse> getAllBogoOffers(@RequestBody Restaurant restaurant){

        if(!restaurantService.isRestaurantPresentinDb(restaurant.getRestaurantId())){
            System.out.println("RESTAURANT-ID NOT FOUND IN DB");
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",null),HttpStatus.OK);

        }

        List<BogoOffer> BogoOfferList = bogoOfferRepository.findByRestaurantId(restaurant.getRestaurantId()).orElse(new ArrayList<>());

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",BogoOfferList),HttpStatus.OK);

    }
}
