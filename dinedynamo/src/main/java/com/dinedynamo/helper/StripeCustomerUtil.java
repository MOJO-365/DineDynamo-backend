package com.dinedynamo.helper;

import com.dinedynamo.dto.StripePayRequestDTO;
import com.stripe.exception.StripeException;
import com.stripe.model.Address;
import com.stripe.model.Customer;
import com.stripe.model.CustomerSearchResult;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerSearchParams;

public class StripeCustomerUtil {

    public static Customer findCustomerByEmail(String email) throws StripeException {
        CustomerSearchParams params =
                CustomerSearchParams
                        .builder()
                        .setQuery("email:'" + email + "'")
                        .build();

        CustomerSearchResult result = Customer.search(params);

        return result.getData().size() > 0 ? result.getData().get(0) : null;
    }

    public static Customer findOrCreateCustomer(StripePayRequestDTO stripePayRequestDTO) throws StripeException {
//        CustomerSearchParams params =
//                CustomerSearchParams
//                        .builder()
//                        .setQuery("email:'" + email + "'")
//                        .build();
//
//        CustomerSearchResult result = Customer.search(params);
//
        Customer customer;

//        Address address = new Address();
//        address.setLine1("123 Main St");
//        address.setCity("City");
//        address.setState("ABC");
//        address.setPostalCode("12345");
//        address.setCountry("AUSTRALIA");

        // If no existing customer was found, create a new record
       // if (result.getData().size() == 0) {

            CustomerCreateParams customerCreateParams = CustomerCreateParams.builder()
                    .setName(stripePayRequestDTO.getCustomerName())
                    .setEmail(stripePayRequestDTO.getCustomerEmail())
                    .setAddress(CustomerCreateParams.Address.builder().
                            setCity(stripePayRequestDTO.getCity()).setLine1(stripePayRequestDTO.getLine1()).
                            setCountry(stripePayRequestDTO.getCountry()).setPostalCode(stripePayRequestDTO.getPostalCode()).
                            setState(stripePayRequestDTO. getState()).build())

                    .build();
            customer = Customer.create(customerCreateParams);
        //} else {
//            customer = result.getData().get(0);
//        }

        return customer;
    }
}