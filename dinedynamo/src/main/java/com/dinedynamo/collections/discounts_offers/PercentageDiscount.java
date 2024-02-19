package com.dinedynamo.collections.discounts_offers;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

    private String percentage;

    private String minValue;

    private String minQty;

    private String maxValue;

    private String[] items;

    private String startingDate;

    private String endingDate;

}
