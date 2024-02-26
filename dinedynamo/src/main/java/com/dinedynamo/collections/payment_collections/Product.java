package com.dinedynamo.collections.payment_collections;


import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private String productId;
    private String productName;
    private Long quantity;
    private String priceId;
    private String currency;
    private Long amount;
}
