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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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


    private List<Object> mergeBills(List<DineInFinalBill> dineInFinalBills, List<DeliveryFinalBill> deliveryFinalBills, List<TakeAwayFinalBill> takeAwayFinalBills) {
        List<Object> mergedBills = new ArrayList<>();

        mergedBills.addAll(dineInFinalBills);
        mergedBills.addAll(deliveryFinalBills);
        mergedBills.addAll(takeAwayFinalBills);

        Collections.sort(mergedBills, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                LocalDateTime dateTime1, dateTime2;
                if (o1 instanceof DineInFinalBill) {
                    dateTime1 = ((DineInFinalBill) o1).getDatetime();
                } else if (o1 instanceof DeliveryFinalBill) {
                    dateTime1 = ((DeliveryFinalBill) o1).getDatetime();
                } else {
                    dateTime1 = ((TakeAwayFinalBill) o1).getDatetime();
                }

                if (o2 instanceof DineInFinalBill) {
                    dateTime2 = ((DineInFinalBill) o2).getDatetime();
                } else if (o2 instanceof DeliveryFinalBill) {
                    dateTime2 = ((DeliveryFinalBill) o2).getDatetime();
                } else {
                    dateTime2 = ((TakeAwayFinalBill) o2).getDatetime();
                }

                return dateTime1.compareTo(dateTime2);
            }
        });

        return mergedBills;
    }
}
