package com.dinedynamo.controllers.crm;
import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.crm.CustomerDataResponse;
import com.dinedynamo.collections.invoice_collections.PastOrderHistoryResponse;
import com.dinedynamo.collections.invoice_collections.PastOrderInfo;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.services.crm.CustomerDataService;
import com.dinedynamo.services.invoice_services.OrderHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
public class CustomerDataController {

    @Autowired
    private  CustomerDataService customerDataService;
    @Autowired
    private OrderHistoryService orderHistoryService;

    @PostMapping("/dinedynamo/customer/crm-data")
    public CustomerDataResponse fetchCustomerData(@RequestBody Restaurant restaurant) {
        return customerDataService.getCustomerData(restaurant);
    }


    @PostMapping("/dinedynamo/customer/order-history")
    public ResponseEntity<ApiResponse> getPastOrderHistory(@RequestBody PastOrderInfo pastOrderInfo) {
        String customerPhone = pastOrderInfo.getCustomerPhone();
        PastOrderHistoryResponse orderHistoryResponse = orderHistoryService.getPastOrderHistory(customerPhone);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "success", orderHistoryResponse));
    }
}
