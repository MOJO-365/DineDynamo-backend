package com.dinedynamo.services.discounts_offers_services;
import com.dinedynamo.collections.discounts_offers.OfferType;
import com.dinedynamo.collections.discounts_offers.PercentageDiscount;
import com.dinedynamo.repositories.discounts_offers_repositories.PercentageDiscountRepository;
import com.dinedynamo.services.restaurant_services.RestaurantService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PercentageDiscountServiceTest {

    @Mock
    private PercentageDiscountRepository percentageDiscountRepository;

    @Mock
    private RestaurantService restaurantService;

    @InjectMocks
    private PercentageDiscountService percentageDiscountService;

    @Test
    void testSavePercentageDiscount() {
        // Arrange
        PercentageDiscount percentageDiscountToSave = PercentageDiscount.builder()
                .discountOfferId("1")
                .restaurantId("restaurant123")
                .offerType(OfferType.PERCENTAGE)
                .offerName("Percentage Off Offer")
                .percentage("10")
                .minValue("20")
                .minQty("2")
                .maxValue("100")
                .items(new String[]{"item1", "item2"})
                .startingDate("2022-01-01")
                .endingDate("2022-12-31")
                .build();

        // Mock the repository's save method
        when(percentageDiscountRepository.save(any(PercentageDiscount.class))).thenReturn(percentageDiscountToSave);

        // Act
        PercentageDiscount savedPercentageDiscount = percentageDiscountService.save(percentageDiscountToSave);

        // Assert
        verify(percentageDiscountRepository, times(1)).save(any(PercentageDiscount.class));
        assertEquals("1", savedPercentageDiscount.getDiscountOfferId());
        assertEquals("restaurant123", savedPercentageDiscount.getRestaurantId());
        assertEquals(OfferType.PERCENTAGE, savedPercentageDiscount.getOfferType());
        assertEquals("Percentage Off Offer", savedPercentageDiscount.getOfferName());
        // Add additional assertions based on your business logic and the expected behavior of the service
    }

    @Test
    void testSavePercentageDiscountWithDefaultValues() {
        // Arrange
        PercentageDiscount percentageDiscountToSave = PercentageDiscount.builder()
                .discountOfferId("2")
                .restaurantId("restaurant456")
                .offerType(OfferType.PERCENTAGE)
                .offerName("Percentage Discount Offer")
                .build();

        // Mock the repository's save method
        when(percentageDiscountRepository.save(any(PercentageDiscount.class))).thenReturn(percentageDiscountToSave);

        // Act
        PercentageDiscount savedPercentageDiscount = percentageDiscountService.save(percentageDiscountToSave);

        // Assert
        verify(percentageDiscountRepository, times(1)).save(any(PercentageDiscount.class));
        assertEquals("2", savedPercentageDiscount.getDiscountOfferId());
        assertEquals("restaurant456", savedPercentageDiscount.getRestaurantId());
        assertEquals(OfferType.PERCENTAGE, savedPercentageDiscount.getOfferType());
        assertEquals("Percentage Discount Offer", savedPercentageDiscount.getOfferName());
    }


}
