package com.dinedynamo.dto.discount_offers_dtos;


import com.dinedynamo.collections.discounts_offers.BogpOffer;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EditBogpOfferDTO {
    String discountOfferId;

    BogpOffer bogpOffer;
}
