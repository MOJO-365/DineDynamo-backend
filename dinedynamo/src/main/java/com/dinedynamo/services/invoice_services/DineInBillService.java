package com.dinedynamo.services.invoice_services;

import com.dinedynamo.collections.invoice_collections.DineInFinalBill;
import com.dinedynamo.repositories.invoice_repositories.DineInBillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DineInBillService {
    @Autowired
    private DineInBillRepository dineInBillRepository;

    public void saveDineInBill(DineInFinalBill dineInFinalBill) {
        dineInBillRepository.save(dineInFinalBill);
    }
}
