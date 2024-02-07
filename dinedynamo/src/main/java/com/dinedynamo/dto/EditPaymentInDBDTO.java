package com.dinedynamo.dto;

import com.dinedynamo.collections.SuccessfulPayment;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditPaymentInDBDTO
{
    String paymentId;

    SuccessfulPayment successfulPayment;
}
