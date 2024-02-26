package com.dinedynamo.controllers.payment_controllers;
import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.payment_collections.Product;
import com.dinedynamo.services.external_services.StripeCheckoutService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@CrossOrigin("*")
public class PaymentController{

    @Value("${stripe.success.url}")
    private String successUrl;

    @Value("${stripe.cancel.url}")
    private String cancelUrl;


    @Autowired
    StripeCheckoutService stripeCheckoutService;

    @PostMapping("dinedynamo/create-checkout-session")
    public ResponseEntity<ApiResponse> createCheckoutSession(@RequestBody List<Product> products) throws StripeException {

        System.out.println(stripeCheckoutService.createCheckoutSession(products));

        return new ResponseEntity<ApiResponse>(new ApiResponse(HttpStatus.OK,"success", stripeCheckoutService.createCheckoutSession(products) ),HttpStatus.OK);


    }

}

