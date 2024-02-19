package com.dinedynamo.dto.discount_offers_dtos;


import com.dinedynamo.collections.discounts_offers.BogoOffer;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EditBogoOfferDTO {

    String discountOfferId;

    BogoOffer bogoOffer;
}
