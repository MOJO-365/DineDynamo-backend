package com.dinedynamo.collections.discounts_offers;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document("bogo_offer")
public class BogoOffer {

    @Id
    private String discountOfferId;

    private String restaurantId;

    private OfferType offerType;

    private String offerName;

    private String buyQty;

    private String getQty;

    private String[] items;

    private String startingDate;

    private String endingDate;
}
