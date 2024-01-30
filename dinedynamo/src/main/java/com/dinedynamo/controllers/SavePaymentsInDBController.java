package com.dinedynamo.controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.SuccessfullPayment;
import com.dinedynamo.repositories.SavePaymentsInDBRepository;
import com.dinedynamo.services.SavePaymentsInDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


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

    @PostMapping("/dinedynamo/save-payment")
    ResponseEntity<ApiResponse> savePaymentDataInDB(@RequestBody SuccessfullPayment successfullPayment){
        successfullPayment = savePaymentsInDBService.save(successfullPayment);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",successfullPayment), HttpStatus.OK);
    }

    @DeleteMapping("/dinedynamo/delete-payment")
    ResponseEntity<ApiResponse> deletePayment(@RequestBody SuccessfullPayment successfullPayment){
        String paymentId = successfullPayment.getPaymentId();
        //If paymentId is null (not passed from the frontend), exception will be thrown
        savePaymentsInDBRepository.delete(successfullPayment);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",successfullPayment), HttpStatus.OK);


    }

}
