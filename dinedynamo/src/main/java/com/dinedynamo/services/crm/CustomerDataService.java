package com.dinedynamo.services.crm;

import com.dinedynamo.collections.invoice_collections.DeliveryFinalBill;
import com.dinedynamo.collections.invoice_collections.TakeAwayFinalBill;
import com.dinedynamo.collections.crm.CustomerDataResponse;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.repositories.invoice_repositories.DeliveryFinalBillRepository;
import com.dinedynamo.repositories.invoice_repositories.TakeAwayFinalBillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerDataService {

    private final DeliveryFinalBillRepository deliveryFinalBillRepository;
    private final TakeAwayFinalBillRepository takeAwayFinalBillRepository;

    @Autowired
    public CustomerDataService(DeliveryFinalBillRepository deliveryFinalBillRepository, TakeAwayFinalBillRepository takeAwayFinalBillRepository) {
        this.deliveryFinalBillRepository = deliveryFinalBillRepository;
        this.takeAwayFinalBillRepository = takeAwayFinalBillRepository;
    }

    public CustomerDataResponse getCustomerData(Restaurant restaurantRequest) {
        String restaurantId = restaurantRequest.getRestaurantId();
        List<DeliveryFinalBill> deliveryFinalBills = deliveryFinalBillRepository.findByRestaurantId(restaurantId);
        List<TakeAwayFinalBill> takeAwayFinalBills = takeAwayFinalBillRepository.findByRestaurantId(restaurantId);
        return new CustomerDataResponse(deliveryFinalBills, takeAwayFinalBills);
    }
}
