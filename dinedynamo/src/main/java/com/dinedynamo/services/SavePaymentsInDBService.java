package com.dinedynamo.services;


import com.dinedynamo.collections.SuccessfulPayment;
import com.dinedynamo.repositories.SavePaymentsInDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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
}
