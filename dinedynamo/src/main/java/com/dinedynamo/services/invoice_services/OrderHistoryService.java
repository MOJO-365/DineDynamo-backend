package com.dinedynamo.services.invoice_services;

import com.dinedynamo.collections.invoice_collections.*;
import com.dinedynamo.repositories.invoice_repositories.DeliveryFinalBillRepository;
import com.dinedynamo.repositories.invoice_repositories.DineInFinalBillRepository;
import com.dinedynamo.repositories.invoice_repositories.TakeAwayFinalBillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@Service
public class OrderHistoryService {

    @Autowired
    private DineInFinalBillRepository dineInFinalBillRepository;

    @Autowired
    private DeliveryFinalBillRepository deliveryFinalBillRepository;

    @Autowired
    private TakeAwayFinalBillRepository takeAwayFinalBillRepository;

    public PastOrderHistoryResponse getPastOrderHistory(String customerPhone) {
        List<PastOrderInfo> pastOrders = new ArrayList<>();

        pastOrders.addAll(mapToPastOrderInfoList(dineInFinalBillRepository.findByCustomerPhone(customerPhone)));
        pastOrders.addAll(mapToPastOrderInfoList(deliveryFinalBillRepository.findByCustomerPhone(customerPhone)));
        pastOrders.addAll(mapToPastOrderInfoList(takeAwayFinalBillRepository.findByCustomerPhone(customerPhone)));

        Collections.sort(pastOrders, Comparator.comparing(PastOrderInfo::getDateTime).reversed());

        return new PastOrderHistoryResponse(pastOrders);
    }

    private List<PastOrderInfo> mapToPastOrderInfoList(List<? extends Object> finalBills) {
        List<PastOrderInfo> pastOrders = new ArrayList<>();
        for (Object bill : finalBills) {
            if (bill instanceof DineInFinalBill) {
                DineInFinalBill dineInBill = (DineInFinalBill) bill;
                pastOrders.add(new PastOrderInfo(
                        dineInBill.getCustomerName(),
                        null,
                        dineInBill.getCustomerPhone(),
                        dineInBill.getOrderType(),
                        dineInBill.getDatetime(),
                        dineInBill.getTotalAmount(),
                        null,
                        dineInBill.getOrderList()
                ));
            } else if (bill instanceof DeliveryFinalBill) {
                DeliveryFinalBill deliveryBill = (DeliveryFinalBill) bill;
                pastOrders.add(new PastOrderInfo(
                        deliveryBill.getCustomerName(),
                        deliveryBill.getCustomerAddress(),
                        deliveryBill.getCustomerPhone(),
                        deliveryBill.getOrderType(),
                        deliveryBill.getDatetime(),
                        deliveryBill.getTotalAmount(),
                        deliveryBill.getCustomerEmail(),
                        deliveryBill.getOrderList()
                ));
            } else if (bill instanceof TakeAwayFinalBill) {
                TakeAwayFinalBill takeAwayBill = (TakeAwayFinalBill) bill;
                pastOrders.add(new PastOrderInfo(
                        takeAwayBill.getCustomerName(),
                        null,
                        takeAwayBill.getCustomerPhone(),
                        takeAwayBill.getOrderType(),
                        takeAwayBill.getDatetime(),
                        takeAwayBill.getTotalAmount(),
                        takeAwayBill.getCustomerEmail(),
                        takeAwayBill.getOrderList()
                ));
            }
        }
        return pastOrders;
    }
}
