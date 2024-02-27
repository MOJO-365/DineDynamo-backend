package com.dinedynamo.services.invoice_services;


import com.dinedynamo.collections.invoice_collections.TakeAwayFinalBill;
import com.dinedynamo.repositories.invoice_repositories.TakeAwayFinalBillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TakeAwayBillService {

    @Autowired
    private TakeAwayFinalBillRepository takeAwayFinalBillRepository;


    public void saveTakeAwayBill(TakeAwayFinalBill takeAwayFinalBill){
        takeAwayFinalBillRepository.save(takeAwayFinalBill);
    }

}
