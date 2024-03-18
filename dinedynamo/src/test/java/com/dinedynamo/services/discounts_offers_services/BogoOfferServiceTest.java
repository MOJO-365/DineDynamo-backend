package com.dinedynamo.services.discounts_offers_services;


import com.dinedynamo.collections.discounts_offers.BogoOffer;
import com.dinedynamo.collections.discounts_offers.OfferType;
import com.dinedynamo.repositories.discounts_offers_repositories.BogoOfferRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BogoOfferServiceTest {

    @Mock
    private BogoOfferRepository bogoOfferRepository;

    @InjectMocks
    private BogoOfferService bogoOfferService;

    @Test
    void testSaveBogoOffer() {
        // Arrange
        BogoOffer bogoOfferToSave = BogoOffer.builder()
                .discountOfferId("1")
                .restaurantId("restaurant123")
                .offerType(OfferType.BOGO)
                .offerName("Buy One Get One Free")
                .buyQty("1")
                .getQty("1")
                .minQty("2")
                .items(new String[]{"item1", "item2"})
                .startingDate("2022-01-01")
                .endingDate("2022-12-31")
                .build();

        // Mock the repository's save method
        when(bogoOfferRepository.save(any(BogoOffer.class))).thenReturn(bogoOfferToSave);

        // Act
        BogoOffer savedBogoOffer = bogoOfferService.save(bogoOfferToSave);

        // Assert
        verify(bogoOfferRepository, times(1)).save(any(BogoOffer.class));
        assertEquals("1", savedBogoOffer.getDiscountOfferId());
        assertEquals("restaurant123", savedBogoOffer.getRestaurantId());
        assertEquals(OfferType.BOGO, savedBogoOffer.getOfferType());
        assertEquals("Buy One Get One Free", savedBogoOffer.getOfferName());
        // Add additional assertions based on your business logic and the expected behavior of the service
    }

    @Test
    void testSaveBogoOfferWithDefaultValues() {
        // Arrange
        BogoOffer bogoOfferToSave = BogoOffer.builder()
                .discountOfferId("2")
                .restaurantId("restaurant456")
                .offerType(OfferType.BOGO)
                .offerName("BOGO Offer")
                .build();

        // Mock the repository's save method
        when(bogoOfferRepository.save(any(BogoOffer.class))).thenReturn(bogoOfferToSave);

        // Act
        BogoOffer savedBogoOffer = bogoOfferService.save(bogoOfferToSave);

        // Assert
        verify(bogoOfferRepository, times(1)).save(any(BogoOffer.class));
        assertEquals("2", savedBogoOffer.getDiscountOfferId());
        assertEquals("restaurant456", savedBogoOffer.getRestaurantId());
        assertEquals(OfferType.BOGO, savedBogoOffer.getOfferType());
        assertEquals("BOGO Offer", savedBogoOffer.getOfferName());
        // Add additional assertions based on your business logic and the expected behavior of the service
    }

    // Add more test methods based on your requirements

}
