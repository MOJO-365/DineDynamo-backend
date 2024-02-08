package com.dinedynamo.controllers;
import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.dto.StripePayRequestDTO;
import com.dinedynamo.helper.StripeCustomerUtil;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class TempPayController {

    @PostMapping("dinedynamo/create-payment-intent")
    public String createPaymentIntent(@RequestBody StripePayRequestDTO stripePayRequestDTO) throws StripeException {

        Stripe.apiKey = "sk_test_51OZ7YrSHVgEh3MIN43YcgXCCwF4tHGLFwAlfrHG7cuC97tVDCWoEeggEBHQWUs8ywW1a91m69XH34DoMCu4ofXbu00XnlHSUX6";

        Customer customer = StripeCustomerUtil.findOrCreateCustomer(stripePayRequestDTO);


        try {
            PaymentIntentCreateParams params =
                    PaymentIntentCreateParams.builder()
                            .setAmount(stripePayRequestDTO.getAmount())
                            .setCurrency("AUD")
                            .setCustomer(customer.getId())
                            .setDescription(stripePayRequestDTO.getDescription())
                            .setAutomaticPaymentMethods(
                                    PaymentIntentCreateParams.AutomaticPaymentMethods
                                            .builder()
                                            .setEnabled(true)
                                            .build()
                            )
                            .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // Send the client secret from the payment intent to the client
            return paymentIntent.getClientSecret();

            //return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",paymentIntent.getClientSecret()),HttpStatus.OK);

        }
        catch (StripeException e) {
            System.out.println("StripeException OCCURRED: "+e.getMessage());
            return null;
            //return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",null),HttpStatus.OK);
        }
    }


//    static String calculateOrderAmount(Product[] items) {
//        long total = 0L;
//
//        for (Product item: items) {
//            // Look up the application database to find the prices for the products in the given list
//            total += ProductDAO.getProduct(item.getId()).getDefaultPriceObject().getUnitAmountDecimal().floatValue();
//        }
//        return String.valueOf(total);
//    }

}