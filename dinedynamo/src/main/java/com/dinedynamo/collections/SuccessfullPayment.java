package com.dinedynamo.collections;

import lombok.*;
import org.json.simple.JSONArray;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document("payments")
public class SuccessfullPayment
{
    @Id
    String paymentId;

    String customerId;

    String customerEmailOrPhone;

    String restaurantId;

    Date dateOfPayment;

    String paymentType;

    double totalAmount;

    JSONArray itemsArray;
}
