package com.dinedynamo.services;


import com.dinedynamo.collections.PaymentResponse;
import com.dinedynamo.collections.Product;
import com.stripe.exception.StripeException;

import org.springframework.stereotype.Service;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

@Service
public class StripeCheckoutService
{
    @Value("${api.stripe.key}")
    private String stripeApiKey;


    @Value("${stripe.success.url}")
    private String successUrl;

    @Value("${stripe.cancel.url}")
    private String cancelUrl;



    //Returns session id
    public PaymentResponse createCheckoutSession(List<Product> products) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();


        for (Product product: products) {
            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                    .setQuantity(product.getQuantity())
                    .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency(product.getCurrency())
                                    .setUnitAmount(product.getAmount())
                                    .setProductData(
                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                    .setName(product.getProductName())
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

            lineItems.add(lineItem);
        }

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .addAllLineItem(lineItems)
//                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
//                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.)
                .setMode(SessionCreateParams.Mode.PAYMENT)

                .build();

        Session session = Session.create(params);

        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setSessionId(session.getId());
        paymentResponse.setSuccessUrl(session.getSuccessUrl());
        paymentResponse.setUrl(session.getUrl());
        System.out.println("Session created");
        return paymentResponse;
    }


    private static List<Product> createStaticProductList() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("1", "Product A", 2L, "price_A", "AUD", 200L));
        products.add(new Product("2", "Product B", 1L, "price_B", "AUD", 150L));
        // Add more products as needed

        return products;
    }
}
