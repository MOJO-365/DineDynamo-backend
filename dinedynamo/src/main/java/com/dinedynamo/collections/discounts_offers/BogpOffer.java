package com.dinedynamo.collections.discounts_offers;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document("bogp_offer")
public class BogpOffer {
    @Id
    private String discountOfferId;

    private String restaurantId;

    private OfferType offerType;

    private String offerName;

    private String minQty;

    private String percentage;

    private String[] items;

    private String startingDate;

    private String endingDate;
}
