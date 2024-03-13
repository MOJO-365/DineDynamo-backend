package com.dinedynamo.services.invoice_services;

import com.dinedynamo.collections.invoice_collections.DeliveryFinalBill;
import com.dinedynamo.repositories.invoice_repositories.DeliveryFinalBillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryBillService {
    @Autowired
    private DeliveryFinalBillRepository deliveryFinalBillRepository;
    public void saveDeliveryBill(DeliveryFinalBill deliveryFinalBill) {
        deliveryFinalBillRepository.save(deliveryFinalBill);
    }
}
