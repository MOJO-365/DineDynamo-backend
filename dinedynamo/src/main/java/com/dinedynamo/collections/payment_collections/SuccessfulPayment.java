package com.dinedynamo.collections.payment_collections;

import lombok.*;
import org.json.simple.JSONArray;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document("payments")
public class SuccessfulPayment
{
    @Id
    String paymentId;

    String customerEmail;

    String customerPhone;

    String customerName;

    String restaurantId;

    Date dateOfPayment;

    String paymentType;

    double totalAmount;

    //If customer splits and pays some amount in cash and some amount with online payment mode
    double cashPayment;

    double onlinePayment;

    JSONArray itemsArray;
}
