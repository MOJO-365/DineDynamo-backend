package com.dinedynamo.services.discounts_offers_services;
import com.dinedynamo.collections.discounts_offers.BogpOffer;
import com.dinedynamo.collections.discounts_offers.OfferType;
import com.dinedynamo.repositories.discounts_offers_repositories.BogpOfferRepository;
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
class BogpOfferServiceTest {

    @Mock
    private BogpOfferRepository bogpOfferRepository;

    @Mock
    private RestaurantService restaurantService;

    @InjectMocks
    private BogpOfferService bogpOfferService;

    @Test
    void testSaveBogpOffer() {
        // Arrange
        BogpOffer bogpOfferToSave = BogpOffer.builder()
                .discountOfferId("1")
                .restaurantId("restaurant123")
                .offerType(OfferType.BOGP)
                .offerName("Buy One Get Percentage Off")
                .minQty("2")
                .maxValue("100")
                .percentage("20")
                .items(new String[]{"item1", "item2"})
                .startingDate("2022-01-01")
                .endingDate("2022-12-31")
                .build();

        // Mock the repository's save method
        when(bogpOfferRepository.save(any(BogpOffer.class))).thenReturn(bogpOfferToSave);

        // Act
        BogpOffer savedBogpOffer = bogpOfferService.save(bogpOfferToSave);

        // Assert
        verify(bogpOfferRepository, times(1)).save(any(BogpOffer.class));
        assertEquals("1", savedBogpOffer.getDiscountOfferId());
        assertEquals("restaurant123", savedBogpOffer.getRestaurantId());
        assertEquals(OfferType.BOGP, savedBogpOffer.getOfferType());
        assertEquals("Buy One Get Percentage Off", savedBogpOffer.getOfferName());
        // Add additional assertions based on your business logic and the expected behavior of the service
    }

    @Test
    void testSaveBogpOfferWithDefaultValues() {
        // Arrange
        BogpOffer bogpOfferToSave = BogpOffer.builder()
                .discountOfferId("2")
                .restaurantId("restaurant456")
                .offerType(OfferType.BOGP)
                .offerName("BOGP Offer")
                .build();

        // Mock the repository's save method
        when(bogpOfferRepository.save(any(BogpOffer.class))).thenReturn(bogpOfferToSave);

        // Act
        BogpOffer savedBogpOffer = bogpOfferService.save(bogpOfferToSave);

        // Assert
        verify(bogpOfferRepository, times(1)).save(any(BogpOffer.class));
        assertEquals("2", savedBogpOffer.getDiscountOfferId());
        assertEquals("restaurant456", savedBogpOffer.getRestaurantId());
        assertEquals(OfferType.BOGP, savedBogpOffer.getOfferType());
        assertEquals("BOGP Offer", savedBogpOffer.getOfferName());
    }



}
