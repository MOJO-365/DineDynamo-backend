package com.dinedynamo.dto.payments_dtos;

import com.dinedynamo.collections.payment_collections.SuccessfulPayment;
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
