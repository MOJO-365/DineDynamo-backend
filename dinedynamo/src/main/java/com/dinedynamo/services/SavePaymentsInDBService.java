package com.dinedynamo.services;


import com.dinedynamo.collections.SuccessfullPayment;
import com.dinedynamo.repositories.SavePaymentsInDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SavePaymentsInDBService
{

    @Autowired
    SavePaymentsInDBRepository savePaymentsInDBRepository;

    public SuccessfullPayment save(SuccessfullPayment successfullPayment){

        successfullPayment.setDateOfPayment(new Date());

        savePaymentsInDBRepository.save(successfullPayment);

        return successfullPayment;
    }
}
