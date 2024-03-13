package com.dinedynamo.services.invoice_services;

import com.dinedynamo.collections.invoice_collections.DineInFinalBill;
import com.dinedynamo.repositories.invoice_repositories.DineInFinalBillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DineInBillService {
    @Autowired
    private DineInFinalBillRepository dineInFinalBillRepository;

    public void saveDineInBill(DineInFinalBill dineInFinalBill) {
        dineInFinalBillRepository.save(dineInFinalBill);
    }
}
