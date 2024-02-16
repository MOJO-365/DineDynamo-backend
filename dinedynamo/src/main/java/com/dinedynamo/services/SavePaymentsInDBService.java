package com.dinedynamo.services;


import com.dinedynamo.collections.SuccessfulPayment;
import com.dinedynamo.dto.EditPaymentInDBDTO;
import com.dinedynamo.repositories.SavePaymentsInDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.NoSuchElementException;

@Service
public class SavePaymentsInDBService
{

    @Autowired
    SavePaymentsInDBRepository savePaymentsInDBRepository;

    public SuccessfulPayment save(SuccessfulPayment successfulPayment){

        successfulPayment.setDateOfPayment(new Date());

        savePaymentsInDBRepository.save(successfulPayment);

        return successfulPayment;
    }

    public SuccessfulPayment editPayment(EditPaymentInDBDTO editPaymentInDBDTO){
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

        return updatedSuccessfulPayment;
    }
}
