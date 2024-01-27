package com.dinedynamo.controllers;
import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.Product;
import com.dinedynamo.services.CheckoutService;
import com.stripe.exception.StripeException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
    CheckoutService checkoutService;

    @PostMapping("dinedynamo/create-checkout-session")
    public ResponseEntity<ApiResponse> createCheckoutSession(@RequestBody List<Product> products) throws StripeException {

        System.out.println(checkoutService.createCheckoutSession(products));

        return new ResponseEntity<ApiResponse>(new ApiResponse(HttpStatus.OK,"success", checkoutService.createCheckoutSession(products) ),HttpStatus.OK);

//        return checkoutService.createCheckoutSession();
    }

}



//@RestController
//public class PaymentController
//{
//
//    @Value("${api.stripe.publicKey}")
//    private String publicKey;
//
//
//    @PostMapping("dinedynamo/create-payment-intent")
//    public PaymentResponse createPaymentIntent(@RequestBody PaymentRequest request)
//            throws StripeException {
//        PaymentIntentCreateParams params =
//                PaymentIntentCreateParams.builder()
//                        .setAmount(request.getAmount())
////                        .putMetadata("productName",
////                                request.getProductName())
//                        .setCurrency("AUD")
//                        .setAutomaticPaymentMethods(
//                                PaymentIntentCreateParams
//                                        .AutomaticPaymentMethods
//                                        .builder()
//                                        .setEnabled(true)
//                                        .build()
//                        )
//                        .build();
//
//
//        PaymentIntent intent =
//                PaymentIntent.create(params);
//        return new PaymentResponse(intent.getId(),
//                intent.getClientSecret());
//    }
//
//
//    @PostMapping("/dinedynamo/get-stripe-attributes")
//    public ResponseEntity<ApiResponse> showCard(@RequestBody PaymentRequest request,
//                                                BindingResult bindingResult){
//
//        if (bindingResult.hasErrors()){
//            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",null),HttpStatus.OK);
//        }
//
//
//        StripeAttributes stripeAttributes = new StripeAttributes();
//        stripeAttributes.setPublicKey(publicKey);
//        stripeAttributes.setAmount(request.getAmount());
//        stripeAttributes.setEmailOrPhone(request.getEmail());
//
//        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",stripeAttributes),HttpStatus.OK);
//
//    }
//
//
//
//}
