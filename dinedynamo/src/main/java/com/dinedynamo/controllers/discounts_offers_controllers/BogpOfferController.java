package com.dinedynamo.controllers.discounts_offers_controllers;

import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.authentication_collections.Restaurant;
import com.dinedynamo.collections.discounts_offers.BogpOffer;
import com.dinedynamo.dto.discount_offers_dtos.EditBogpOfferDTO;
import com.dinedynamo.repositories.discounts_offers_repositories.BogpOfferRepository;
import com.dinedynamo.services.RestaurantService;
import com.dinedynamo.services.discounts_offers_services.BogpOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@CrossOrigin("*")
public class BogpOfferController
{
    @Autowired
    RestaurantService restaurantService;

    @Autowired
    BogpOfferRepository bogpOfferRepository;

    @Autowired
    BogpOfferService bogpOfferService;


    @PostMapping("/dinedynamo/restaurant/offers/create-bogp-offer")
    ResponseEntity<ApiResponse> createBogpOffer(@RequestBody BogpOffer bogpOffer){

        bogpOffer = bogpOfferService.save(bogpOffer);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",bogpOffer),HttpStatus.OK);

    }

    @DeleteMapping("/dinedynamo/restaurant/offers/delete-bogp-offer")
    ResponseEntity<ApiResponse> deleteBogpOffer(@RequestBody BogpOffer bogpOffer){
        if(bogpOffer.getDiscountOfferId() == null){
            System.out.println("DISCOUNT/OFFER ID NOT FOUND IN REQUEST BODY");
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_ACCEPTABLE,"success",null),HttpStatus.OK);

        }
        else{
            bogpOfferRepository.delete(bogpOffer);
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_ACCEPTABLE,"success",bogpOffer),HttpStatus.OK);
        }
    }

    @PutMapping("/dinedynamo/restaurant/offers/edit-bogp-offer")
    ResponseEntity<ApiResponse> editBogpOffer(@RequestBody EditBogpOfferDTO editBogpOfferDTO){

        BogpOffer newBogpOffer = (BogpOffer) editBogpOfferDTO.getBogpOffer();

        newBogpOffer.setDiscountOfferId(editBogpOfferDTO.getDiscountOfferId());

        bogpOfferService.save(newBogpOffer);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",newBogpOffer),HttpStatus.OK);
    }

    @PostMapping("/dinedynamo/restaurant/offers/get-bogp-offer-by-id")
    ResponseEntity<ApiResponse> getBogpOfferById(@RequestParam String discountOfferId){
        BogpOffer bogpOffer = bogpOfferRepository.findById(discountOfferId).orElse(null);

        if(bogpOffer == null){
            System.out.println("SUCH BOGP OFFER NOT FOUND IN DB");
        }

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",bogpOffer),HttpStatus.OK);

    }


    @PostMapping("/dinedynamo/restaurant/offers/get-all-bogp-offers")
    ResponseEntity<ApiResponse> getAllBogpOffers(@RequestBody Restaurant restaurant){

        if(!restaurantService.isRestaurantPresentinDb(restaurant.getRestaurantId())){
            System.out.println("RESTAURANT-ID NOT FOUND IN DB");
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",null),HttpStatus.OK);

        }

        List<BogpOffer> BogpOfferList = bogpOfferRepository.findByRestaurantId(restaurant.getRestaurantId()).orElse(new ArrayList<>());

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",BogpOfferList),HttpStatus.OK);


    }
}
