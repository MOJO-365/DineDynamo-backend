package com.dinedynamo.controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.SuccessfullPayment;
import com.dinedynamo.dto.EditPaymentInDBDTO;
import com.dinedynamo.repositories.SavePaymentsInDBRepository;
import com.dinedynamo.services.SavePaymentsInDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PutMapping("dinedynamo/edit-payment")
    ResponseEntity<ApiResponse> editPayment(@RequestBody EditPaymentInDBDTO editPaymentInDBDTO){

        String paymentId = editPaymentInDBDTO.getPaymentId();

        if(paymentId == null || paymentId.equals("") || paymentId.equals(" ")){
            System.out.println("PAYMENT ID SHOULD BE PASSED IN REQUEST FOR EDIT PAYMENT");
            throw new RuntimeException("PaymentId not the request body");
        }
        SuccessfullPayment successfullPayment = savePaymentsInDBRepository.findById(paymentId).orElse(null);

        if(successfullPayment == null){
            System.out.println("RECORD WITH SUCH PAYMENT-ID DOES NOT EXIST IN DB");
            throw new NoSuchElementException("Record with paymentId not found in db");
        }
        SuccessfullPayment updatedSuccessfullPayment = editPaymentInDBDTO.getSuccessfullPayment();

        updatedSuccessfullPayment.setPaymentId(paymentId);

        savePaymentsInDBRepository.save(updatedSuccessfullPayment);


        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",updatedSuccessfullPayment),HttpStatus.OK);

    }


}
