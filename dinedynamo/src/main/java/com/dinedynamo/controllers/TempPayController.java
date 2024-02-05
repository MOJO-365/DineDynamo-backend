package com.dinedynamo.controllers;
import com.dinedynamo.api.ApiResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TempPayController {

    @PostMapping("dinedynamo/create-payment-intent")
    public ResponseEntity<ApiResponse> createPaymentIntent() {

        Stripe.apiKey = "sk_test_51OZ7YrSHVgEh3MIN43YcgXCCwF4tHGLFwAlfrHG7cuC97tVDCWoEeggEBHQWUs8ywW1a91m69XH34DoMCu4ofXbu00XnlHSUX6";

        try {
            PaymentIntentCreateParams params = new PaymentIntentCreateParams.Builder()
                    .setCurrency("AUD")
                    .setAmount(1000L)
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods
                                    .builder()
                                    .setEnabled(true)
                                    .build()
                    )
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",paymentIntent.getClientSecret()),HttpStatus.OK);

        }
        catch (StripeException e) {
            System.out.println("StripeException OCCURRED");
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",null),HttpStatus.OK);
        }
    }
}