package com.dinedynamo.dto;

import com.dinedynamo.collections.SuccessfullPayment;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditPaymentInDBDTO
{
    String paymentId;

    SuccessfullPayment successfullPayment;
}
