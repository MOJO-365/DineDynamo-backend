package com.dinedynamo.repositories.reservation_repositories;

import com.dinedynamo.collections.reservation_collections.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {

    @Query("{ 'restaurantId' : '?0'}")
    Optional<List<Reservation>> findByRestaurantId(String restaurantId);



    //For restaurant side
    @Query("{ 'restaurantId' : '?0', 'reservationRequestStatus' : 'HOLD' }")
    Optional<List<Reservation>> findHoldReservationsByRestaurantId(String restaurantId);

    //For restaurant side
    @Query("{ 'restaurantId' : '?0', 'reservationRequestStatus' : 'CANCELLED' }")
    Optional<List<Reservation>> findCancelledReservationsByRestaurantId(String restaurantId);


    //For restaurant side
    @Query("{ 'restaurantId' : '?0', 'reservationRequestStatus' : 'ACCEPTED' }")
    Optional<List<Reservation>> findAcceptedReservationsByRestaurantId(String restaurantId);


    //For customer side
    @Query("{ 'restaurantId' : '?0', 'reservationRequestStatus' : 'ACCEPTED', 'customerPhone' : '?1' }")
    Optional<List<Reservation>> findAcceptedReservationsByRestaurantIdAndCustomerPhone(String restaurantId, String customerPhone);

    //For customer side
    @Query("{ 'restaurantId' : '?0', 'reservationRequestStatus' : 'HOLD', 'customerPhone' : '?1' }")
    Optional<List<Reservation>> findHoldReservationsByRestaurantIdAndCustomerPhone(String restaurantId, String customerPhone);



    //For customer side
    @Query("{ 'restaurantId' : '?0', 'reservationRequestStatus' : 'REJECTED', 'customerPhone' : '?1' }")
    Optional<List<Reservation>> findRejectedReservationsByRestaurantIdAndCustomerPhone(String restaurantId, String customerPhone);



    @Query("{ 'restaurantId' : '?0', 'customerPhone' : '?1' }")
    Optional<List<Reservation>> findByRestaurantIdAndCustomerPhone(String restaurantId, String customerPhone);



}
