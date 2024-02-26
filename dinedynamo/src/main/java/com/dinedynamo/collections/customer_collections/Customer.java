package com.dinedynamo.collections.customer_collections;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document("customer")
public class Customer
{
    @Id
    String customerId;
    String customerName;
    @Indexed(unique = true)
    String customerEmail;
    String customerPassword;

}
