package com.dinedynamo.services.reservation_services;
import com.dinedynamo.collections.reservation_collections.Reservation;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.helper.DateTimeUtility;
import com.dinedynamo.repositories.reservation_repositories.ReservationRepository;
import com.dinedynamo.repositories.restaurant_repositories.RestaurantRepository;
import com.dinedynamo.services.external_services.SmsService;
import com.dinedynamo.services.restaurant_services.RestaurantService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantReservationServiceTest {

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private SmsService smsService;

    @Mock
    private RestaurantRepository restaurantRepository;



    @InjectMocks
    private RestaurantReservationService restaurantReservationService;

    @Test
    void testAcceptReservationOfCustomer() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setRestaurantId("restaurant123");
        reservation.setReservationId("reservation123");
        reservation.setCustomerPhone("1234567890");

        when(restaurantService.isRestaurantPresentinDb("restaurant123")).thenReturn(true);
        when(reservationRepository.save(any())).thenReturn(reservation);

        // Act
        Reservation result = restaurantReservationService.acceptReservationOfCustomer(reservation, "Accepted!");

        // Assert
        assertEquals(Reservation.ReservationRequestStatus.ACCEPTED, result.getReservationRequestStatus());
        verify(reservationRepository, times(1)).save(reservation);
        verify(smsService, times(1)).sendMessageToCustomer("1234567890", "Accepted!");
    }

    @Test
    void testRejectReservationOfCustomer() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setRestaurantId("restaurant123");
        reservation.setReservationId("reservation123");
        reservation.setCustomerPhone("1234567890");

        when(restaurantService.isRestaurantPresentinDb("restaurant123")).thenReturn(true);
        when(reservationRepository.save(any())).thenReturn(reservation);

        // Act
        Reservation result = restaurantReservationService.rejectReservationOfCustomer(reservation, "Rejected!");

        // Assert
        assertEquals(Reservation.ReservationRequestStatus.REJECTED, result.getReservationRequestStatus());
        verify(reservationRepository, times(1)).save(reservation);
        verify(smsService, times(1)).sendMessageToCustomer("1234567890", "Rejected!");
    }

    @Test
    void testClearOldReservationsFromDb() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId("restaurant123");

        Reservation reservation = new Reservation();
        reservation.setRestaurantId("restaurant123");
        reservation.setDineInDate(LocalDate.now().minusDays(1));

        List<Reservation> reservations = Collections.singletonList(reservation);

        when(restaurantRepository.findById("restaurant123")).thenReturn(Optional.of(restaurant));
        when(reservationRepository.findByRestaurantId("restaurant123")).thenReturn(Optional.of(reservations));
        doNothing().when(reservationRepository).delete(any());

        // Act
        boolean result = restaurantReservationService.clearOldReservationsFromDb("restaurant123");

        // Assert
        assertTrue(result);
        verify(reservationRepository, times(1)).delete(reservation);
    }

    @Test
    void testDeleteCancelledReservation() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setReservationId("reservation1234");
        reservation.setReservationRequestStatus(Reservation.ReservationRequestStatus.CANCELLED);

        when(reservationRepository.findById("reservation1234")).thenReturn(Optional.of(reservation));
        doNothing().when(reservationRepository).delete(reservation);

        // Act
        Reservation result = restaurantReservationService.deleteCancelledReservation(reservation);

        // Assert

        assertEquals(result, reservation);
        verify(reservationRepository, times(1)).delete(reservation);
    }


}
