package com.dinedynamo.collections;


import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse
{
    String sessionId;

    String successUrl;

    String url;
}
