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
@Document("percentage_discount")
public class PercentageDiscount
{
    @Id
    private String discountOfferId;

    private String restaurantId;

    private OfferType offerType;

    private String offerName;

    @Builder.Default
    private String percentage=" ";

    @Builder.Default
    private String minValue=" ";

    @Builder.Default
    private String minQty=" ";

    @Builder.Default
    private String maxValue=" ";

    @Builder.Default
    private String[] items = {};

    @Builder.Default
    private String startingDate=" ";

    @Builder.Default
    private String endingDate=" ";

}
