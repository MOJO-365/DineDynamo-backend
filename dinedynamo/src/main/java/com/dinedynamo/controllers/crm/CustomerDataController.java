package com.dinedynamo.controllers.crm;
import com.dinedynamo.collections.crm.CustomerDataResponse;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.services.crm.CustomerDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
public class CustomerDataController {

    @Autowired
    private  CustomerDataService customerDataService;

    @PostMapping("/dinedynamo/customer/crm-data")
    public CustomerDataResponse fetchCustomerData(@RequestBody Restaurant restaurant) {
        return customerDataService.getCustomerData(restaurant);
    }

}
