package com.dinedynamo.controllers.payment_controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.authentication_collections.Restaurant;
import com.dinedynamo.collections.payment_collections.SuccessfulPayment;
import com.dinedynamo.dto.payments_dtos.EditPaymentInDBDTO;
import com.dinedynamo.repositories.SavePaymentsInDBRepository;
import com.dinedynamo.services.SavePaymentsInDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Purpose of this controller: when the payment is done (Online or cash), the details of particular customer and total amount will be saved in the database
 * Saving the details in database if for report and analysis purpose.
 */
@RestController
@CrossOrigin("*")

public class SavePaymentsInDBController
{
    @Autowired
    SavePaymentsInDBService savePaymentsInDBService;

    @Autowired
    SavePaymentsInDBRepository savePaymentsInDBRepository;


    /**
     *
     * @param successfulPayment
     * @return successfulPayment
     * Use: When the payment is done by the customer, this api needs to be called for saving details in DB
     */
    @PostMapping("/dinedynamo/restaurant/payments/save-payment")
    ResponseEntity<ApiResponse> savePaymentDataInDB(@RequestBody SuccessfulPayment successfulPayment){
        successfulPayment = savePaymentsInDBService.save(successfulPayment);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success", successfulPayment), HttpStatus.OK);
    }

    /**
     *
     * @param successfulPayment
     * @return successfulPayment
     * Use: deleting the payment data from database
     */
    @DeleteMapping("/dinedynamo/restaurant/payments/delete-payment")
    ResponseEntity<ApiResponse> deletePayment(@RequestBody SuccessfulPayment successfulPayment){
        String paymentId = successfulPayment.getPaymentId();
        //If paymentId is null (not passed from the frontend), exception will be thrown
        savePaymentsInDBRepository.delete(successfulPayment);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success", successfulPayment), HttpStatus.OK);

    }

    /**
     *
     * @param editPaymentInDBDTO
     * @return successfulPayment
     * Use: For updating the payment data
     */
    @PutMapping("/dinedynamo/restaurant/payments/edit-payment")
    ResponseEntity<ApiResponse> editPayment(@RequestBody EditPaymentInDBDTO editPaymentInDBDTO){

        SuccessfulPayment updatedSuccessfulPayment = savePaymentsInDBService.editPayment(editPaymentInDBDTO);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success", updatedSuccessfulPayment),HttpStatus.OK);

    }


    /**
     *
     * @param restaurant
     * @return List of SuccessfulPayment objects
     * Use: fetch all the SuccessfulPayment done to a particular restaurant
     */
    @PostMapping("/dinedynamo/restaurant/payments/get-all-payments")
    ResponseEntity<ApiResponse> getAllPaymentsOfRestaurant(@RequestBody Restaurant restaurant){

        List<SuccessfulPayment> payments = savePaymentsInDBRepository.findByRestaurantId(restaurant.getRestaurantId()).orElse(new ArrayList<>());

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",payments),HttpStatus.OK);
    }



}
