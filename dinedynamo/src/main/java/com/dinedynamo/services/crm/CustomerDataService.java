package com.dinedynamo.services.crm;

import com.dinedynamo.collections.crm.CustomerDataResponse;
import com.dinedynamo.collections.invoice_collections.DeliveryFinalBill;
import com.dinedynamo.collections.invoice_collections.DineInFinalBill;
import com.dinedynamo.collections.invoice_collections.TakeAwayFinalBill;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.repositories.invoice_repositories.DeliveryFinalBillRepository;
import com.dinedynamo.repositories.invoice_repositories.DineInFinalBillRepository;
import com.dinedynamo.repositories.invoice_repositories.TakeAwayFinalBillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerDataService {

    @Autowired
    private DineInFinalBillRepository dineInFinalBillRepository;
    @Autowired
    private DeliveryFinalBillRepository deliveryFinalBillRepository;

    @Autowired
    private TakeAwayFinalBillRepository takeAwayFinalBillRepository;

    public CustomerDataResponse getCustomerData(Restaurant restaurantRequest) {
        String restaurantId = restaurantRequest.getRestaurantId();

        List<DineInFinalBill> dineInFinalBills = dineInFinalBillRepository.findByRestaurantId(restaurantId);
        List<DeliveryFinalBill> deliveryFinalBills = deliveryFinalBillRepository.findByRestaurantId(restaurantId);
        List<TakeAwayFinalBill> takeAwayFinalBills = takeAwayFinalBillRepository.findByRestaurantId(restaurantId);
        List<Object> mergedBills = mergeBills(dineInFinalBills,deliveryFinalBills, takeAwayFinalBills);

        return new CustomerDataResponse(mergedBills);
    }

    private List<Object> mergeBills(List<DineInFinalBill> dineInFinalBills,List<DeliveryFinalBill> deliveryFinalBills, List<TakeAwayFinalBill> takeAwayFinalBills) {
        List<Object> mergedBills = new ArrayList<>();

        mergedBills.addAll(dineInFinalBills);

        mergedBills.addAll(deliveryFinalBills);

        mergedBills.addAll(takeAwayFinalBills);

        return mergedBills;
    }
}
