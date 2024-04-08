package com.dinedynamo.services.crm;

import com.dinedynamo.collections.crm.CustomerDataResponse;
import com.dinedynamo.collections.invoice_collections.DeliveryFinalBill;
import com.dinedynamo.collections.invoice_collections.DineInFinalBill;
import com.dinedynamo.collections.invoice_collections.TakeAwayFinalBill;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.collections.crm.CustomerOrderInfo;
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
public class CustomerDataService {

    @Autowired
    private DineInFinalBillRepository dineInFinalBillRepository;

    @Autowired
    private DeliveryFinalBillRepository deliveryFinalBillRepository;

    @Autowired
    private TakeAwayFinalBillRepository takeAwayFinalBillRepository;

    public CustomerDataResponse getCustomerData(Restaurant restaurantRequest) {
        String restaurantId = restaurantRequest.getRestaurantId();

        List<CustomerOrderInfo> customerOrders = new ArrayList<>();

        List<Object> allFinalBills = new ArrayList<>();
        allFinalBills.addAll(dineInFinalBillRepository.findByRestaurantId(restaurantId));
        allFinalBills.addAll(deliveryFinalBillRepository.findByRestaurantId(restaurantId));
        allFinalBills.addAll(takeAwayFinalBillRepository.findByRestaurantId(restaurantId));

        customerOrders.addAll(mapToCustomerOrderInfoList(allFinalBills));

        Collections.sort(customerOrders, Comparator.comparing(CustomerOrderInfo::getDate));

        return new CustomerDataResponse(customerOrders);
    }

    private List<CustomerOrderInfo> mapToCustomerOrderInfoList(List<? extends Object> finalBills) {
        List<CustomerOrderInfo> customerOrders = new ArrayList<>();
        for (Object bill : finalBills) {
            if (bill instanceof DineInFinalBill) {
                DineInFinalBill dineInBill = (DineInFinalBill) bill;
                customerOrders.add(new CustomerOrderInfo(
                        dineInBill.getCustomerName(),
                        null,
                        dineInBill.getCustomerPhone(),
                        dineInBill.getOrderType(),
                        dineInBill.getDatetime(),
                        dineInBill.getTotalAmount(),
                        null
                ));
            } else if (bill instanceof DeliveryFinalBill) {
                DeliveryFinalBill deliveryBill = (DeliveryFinalBill) bill;
                customerOrders.add(new CustomerOrderInfo(
                        deliveryBill.getCustomerName(),
                        deliveryBill.getCustomerAddress(),
                        deliveryBill.getCustomerPhone(),
                        deliveryBill.getOrderType(),
                        deliveryBill.getDatetime(),
                        deliveryBill.getTotalAmount(),
                        deliveryBill.getCustomerEmail()
                ));
            } else if (bill instanceof TakeAwayFinalBill) {
                TakeAwayFinalBill takeAwayBill = (TakeAwayFinalBill) bill;
                customerOrders.add(new CustomerOrderInfo(
                        takeAwayBill.getCustomerName(),
                        null,
                        takeAwayBill.getCustomerPhone(),
                        takeAwayBill.getOrderType(),
                        takeAwayBill.getDatetime(),
                        takeAwayBill.getTotalAmount(),
                        takeAwayBill.getCustomerEmail()
                ));
            }
        }
        return customerOrders;
    }
}
