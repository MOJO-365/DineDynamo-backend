package com.dinedynamo.services.reservation_services;
import com.dinedynamo.collections.reservation_collections.Reservation;
import com.dinedynamo.collections.reservation_collections.RestaurantReservationSettings;
import com.dinedynamo.repositories.reservation_repositories.ReservationRepository;
import com.dinedynamo.repositories.reservation_repositories.RestaurantReservationSettingsRepository;
import com.mongodb.client.MongoClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerReservationServiceTest {


    @Mock
    private MongoClient mongoClient;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RestaurantReservationSettingsRepository restaurantReservationSettingsRepository;

    @InjectMocks
    private CustomerReservationService customerReservationService;

    @Test
    void testValidateReservationRequestValid() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setGuestCount(2);
        reservation.setCustomerPhone("1234567890");
        reservation.setCustomerName("John Doe");
        reservation.setDineInDate(LocalDate.now());
        reservation.setRestaurantId("restaurant123");

        // Act
        boolean result = customerReservationService.validateReservationRequest(reservation);

        // Assert
        assertTrue(result);
    }

    @Test
    void testValidateReservationRequestInvalid() {
        // Arrange
        Reservation reservation = new Reservation();

        // Act
        boolean result = customerReservationService.validateReservationRequest(reservation);

        // Assert
        assertFalse(result);
    }

    @Test
    void testSaveReservationInvalidDate() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setDineInDate(LocalDate.now().minusDays(1));

        // Act
        Reservation result = customerReservationService.save(reservation);

        // Assert
        assertEquals(Reservation.ReservationRequestStatus.INVALID, result.getReservationRequestStatus());
    }

    // Add more test methods for other scenarios

    @Test
    void testGetSlotFirstSlot() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setDineInTime(LocalTime.of(12, 0));
        RestaurantReservationSettings restaurantReservationSettings = new RestaurantReservationSettings();
        restaurantReservationSettings.setFirstSlotStartTime(LocalTime.of(11, 0));
        restaurantReservationSettings.setFirstSlotEndTime(LocalTime.of(14, 0));

        when(restaurantReservationSettingsRepository.findByRestaurantId(any())).thenReturn(Optional.of(restaurantReservationSettings));

        // Act
        int result = customerReservationService.getSlot(reservation);

        // Assert
        assertEquals(1, result);
    }

    @Test
    void testGetSlotSecondSlot() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setDineInTime(LocalTime.of(18, 0));
        RestaurantReservationSettings restaurantReservationSettings = new RestaurantReservationSettings();
        restaurantReservationSettings.setSecondSlotStartTime(LocalTime.of(17, 0));
        restaurantReservationSettings.setSecondSlotEndTime(LocalTime.of(20, 0));

        when(restaurantReservationSettingsRepository.findByRestaurantId(any())).thenReturn(Optional.of(restaurantReservationSettings));

        // Act
        int result = customerReservationService.getSlot(reservation);

        // Assert
        assertEquals(2, result);
    }

    @Test
    void testIsPresentInSecondSlot() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setRestaurantId("restaurant123");
        reservation.setCustomerPhone("1234567890");
        reservation.setDineInDate(LocalDate.now());
        reservation.setDineInTime(LocalTime.of(18, 30));

        Reservation existingReservation = new Reservation();
        existingReservation.setDineInDate(LocalDate.now());
        existingReservation.setDineInTime(LocalTime.of(17, 0));
        existingReservation.setReservationRequestStatus(Reservation.ReservationRequestStatus.ACCEPTED);

        RestaurantReservationSettings restaurantReservationSettings = new RestaurantReservationSettings();
        restaurantReservationSettings.setSecondSlotStartTime(LocalTime.of(16, 0));
        restaurantReservationSettings.setSecondSlotEndTime(LocalTime.of(20, 0));

        List<Reservation> existingReservations = Arrays.asList(existingReservation);

        when(reservationRepository.findByRestaurantIdAndCustomerPhone(any(), any())).thenReturn(Optional.of(existingReservations));
        when(restaurantReservationSettingsRepository.findByRestaurantId(any())).thenReturn(Optional.of(restaurantReservationSettings));

        // Act
        boolean result = customerReservationService.isPresentInSecondSlot(reservation);

        // Assert
        assertTrue(result);
    }


    @Test
    void testIsPresentInFirstSlot() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setRestaurantId("restaurant123");
        reservation.setDineInDate(LocalDate.now());
        reservation.setDineInTime(LocalTime.of(17, 30));

        Reservation existingReservationInFirstSlot = new Reservation();
        existingReservationInFirstSlot.setDineInDate(LocalDate.now());
        existingReservationInFirstSlot.setDineInTime(LocalTime.of(16, 0));

        List<Reservation> existingReservations = Arrays.asList(existingReservationInFirstSlot);

        RestaurantReservationSettings restaurantReservationSettings = new RestaurantReservationSettings();
        restaurantReservationSettings.setFirstSlotStartTime(LocalTime.of(15, 0));
        restaurantReservationSettings.setFirstSlotEndTime(LocalTime.of(18, 0));

        when(reservationRepository.findByRestaurantIdAndCustomerPhone(any(), any())).thenReturn(Optional.of(existingReservations));
        when(restaurantReservationSettingsRepository.findByRestaurantId(any())).thenReturn(Optional.of(restaurantReservationSettings));

        // Act
        boolean result = customerReservationService.isPresentInFirstSlot(reservation);

        // Assert
        assertTrue(result);
    }

}
