package com.dinedynamo.controllers.crm;
import com.dinedynamo.collections.crm.CustomerDataResponse;
import com.dinedynamo.collections.invoice_collections.DeliveryFinalBill;
import com.dinedynamo.collections.invoice_collections.TakeAwayFinalBill;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.services.crm.CustomerDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerDataController {

    @Autowired
    private  CustomerDataService customerDataService;

    @PostMapping("/dinedynamo/crm/fetch")
    public CustomerDataResponse fetchCustomerData(@RequestBody Restaurant restaurant) {
        return customerDataService.getCustomerData(restaurant);
    }


}
