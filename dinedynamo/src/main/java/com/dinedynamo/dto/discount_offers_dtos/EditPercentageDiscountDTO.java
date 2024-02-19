package com.dinedynamo.dto.discount_offers_dtos;


import com.dinedynamo.collections.discounts_offers.PercentageDiscount;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EditPercentageDiscountDTO
{
    String discountOfferId;

    PercentageDiscount percentageDiscount;
}
