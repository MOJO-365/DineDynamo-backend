package com.dinedynamo.controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.Restaurant;
import com.dinedynamo.collections.SuccessfulPayment;
import com.dinedynamo.dto.EditPaymentInDBDTO;
import com.dinedynamo.repositories.SavePaymentsInDBRepository;
import com.dinedynamo.services.SavePaymentsInDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


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

    @PostMapping("/dinedynamo/restaurant/payments/save-payment")
    ResponseEntity<ApiResponse> savePaymentDataInDB(@RequestBody SuccessfulPayment successfulPayment){
        successfulPayment = savePaymentsInDBService.save(successfulPayment);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success", successfulPayment), HttpStatus.OK);
    }

    @DeleteMapping("/dinedynamo/restaurant/payments/delete-payment")
    ResponseEntity<ApiResponse> deletePayment(@RequestBody SuccessfulPayment successfulPayment){
        String paymentId = successfulPayment.getPaymentId();
        //If paymentId is null (not passed from the frontend), exception will be thrown
        savePaymentsInDBRepository.delete(successfulPayment);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success", successfulPayment), HttpStatus.OK);

    }

    @PutMapping("/dinedynamo/restaurant/payments/edit-payment")
    ResponseEntity<ApiResponse> editPayment(@RequestBody EditPaymentInDBDTO editPaymentInDBDTO){

        String paymentId = editPaymentInDBDTO.getPaymentId();

        if(paymentId == null || paymentId.equals("") || paymentId.equals(" ")){
            System.out.println("PAYMENT ID SHOULD BE PASSED IN REQUEST FOR EDIT PAYMENT");
            throw new RuntimeException("PaymentId not the request body");
        }
        SuccessfulPayment successfulPayment = savePaymentsInDBRepository.findById(paymentId).orElse(null);

        if(successfulPayment == null){
            System.out.println("RECORD WITH SUCH PAYMENT-ID DOES NOT EXIST IN DB");
            throw new NoSuchElementException("Record with paymentId not found in db");
        }
        SuccessfulPayment updatedSuccessfulPayment = editPaymentInDBDTO.getSuccessfulPayment();

        updatedSuccessfulPayment.setPaymentId(paymentId);

        savePaymentsInDBRepository.save(updatedSuccessfulPayment);


        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success", updatedSuccessfulPayment),HttpStatus.OK);

    }


    @PostMapping("/dinedynamo/restaurant/payments/get-all-payments")
    ResponseEntity<ApiResponse> getAllPaymentsOfRestaurant(@RequestBody Restaurant restaurant){


        List<SuccessfulPayment> payments = savePaymentsInDBRepository.findByRestaurantId(restaurant.getRestaurantId()).orElse(new ArrayList<>());

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",payments),HttpStatus.OK);
    }



}
