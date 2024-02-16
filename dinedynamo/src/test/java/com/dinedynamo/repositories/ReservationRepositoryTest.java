package com.dinedynamo.repositories;

import com.dinedynamo.collections.Reservation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void testFindByRestaurantId() {
        String restaurantId = "123";
        Reservation reservation1 = createReservation(restaurantId);
        Reservation reservation2 = createReservation(restaurantId);
        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);

        Optional<List<Reservation>> result = reservationRepository.findByRestaurantId(restaurantId);

        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
    }

    @Test
    void testFindHoldReservationsByRestaurantId() {
        String restaurantId = "123";
        Reservation reservation1 = createReservationWithStatus(restaurantId, Reservation.ReservationRequestStatus.HOLD);
        Reservation reservation2 = createReservationWithStatus(restaurantId, Reservation.ReservationRequestStatus.HOLD);
        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);

        Optional<List<Reservation>> result = reservationRepository.findHoldReservationsByRestaurantId(restaurantId);

        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
    }

    @Test
    void testFindAcceptedReservationsByRestaurantId() {
        String restaurantId = "123";
        Reservation reservation1 = createReservationWithStatus(restaurantId, Reservation.ReservationRequestStatus.ACCEPTED);
        Reservation reservation2 = createReservationWithStatus(restaurantId, Reservation.ReservationRequestStatus.ACCEPTED);
        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);

        Optional<List<Reservation>> result = reservationRepository.findAcceptedReservationsByRestaurantId(restaurantId);

        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
    }


    private Reservation createReservation(String restaurantId) {
        Reservation reservation = new Reservation();
        reservation.setRestaurantId(restaurantId);

        return reservation;
    }

    private Reservation createReservationWithStatus(String restaurantId, Reservation.ReservationRequestStatus status) {
        Reservation reservation = createReservation(restaurantId);
        reservation.setReservationRequestStatus(status);
        return reservation;
    }
}