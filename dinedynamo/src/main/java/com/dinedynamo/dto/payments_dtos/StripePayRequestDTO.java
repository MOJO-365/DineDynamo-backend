package com.dinedynamo.dto.payments_dtos;

import com.stripe.model.Customer;
import com.stripe.model.Product;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StripePayRequestDTO
{
    //Product[] items;
    String customerName;
    String customerEmail;
    String line1;
    String city;
    String country;
    String postalCode;
    String state;
    String description;
    Long amount;


}
