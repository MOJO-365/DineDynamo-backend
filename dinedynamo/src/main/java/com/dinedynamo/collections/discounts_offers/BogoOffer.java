package com.dinedynamo.collections.discounts_offers;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Builder
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

    @Builder.Default
    private String buyQty=" ";

    @Builder.Default
    private String getQty=" ";

    @Builder.Default
    private String minQty=" ";

    @Builder.Default
    private String[] items={};

    @Builder.Default
    private String startingDate=" ";

    @Builder.Default
    private String endingDate=" ";
}
