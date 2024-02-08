package com.dinedynamo.services;

import com.dinedynamo.collections.Reservation;
import com.dinedynamo.collections.ReservationRequestStatus;
import com.dinedynamo.collections.Restaurant;
import com.dinedynamo.repositories.ReservationRepository;
import com.dinedynamo.repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class RestaurantReservationService
{
    @Autowired
    RestaurantService restaurantService;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    SmsService smsService;

    @Autowired
    RestaurantRepository restaurantRepository;


    public Reservation acceptReservationOfCustomer(Reservation reservation, String messageContent){

        boolean isRestaurantPresentInDb = restaurantService.isRestaurantPresentinDb(reservation.getRestaurantId());
        if(!isRestaurantPresentInDb){

            System.out.println("RESTAURANT-ID NOT FOUND IN DB");
            throw new RuntimeException("RESTAURANT-ID NOT FOUND IN DB");
        }

        if(reservation.getReservationId() == null || reservation.getReservationId() == "" || reservation.getReservationId() == " "){

            System.out.println("PASS PROPER RESERVATION-ID");
            throw new RuntimeException("RESERVATION-ID IMPROPER");
        }

        reservation.setReservationRequestStatus(ReservationRequestStatus.ACCEPTED);
        reservationRepository.save(reservation);

        System.out.println("Ready to send sms");
        smsService.sendMessageToCustomer(reservation.getCustomerPhone(),messageContent);
        return reservation;
    }


    public Reservation rejectReservationOfCustomer(Reservation reservation, String messageContent){

        boolean isRestaurantPresentInDb = restaurantService.isRestaurantPresentinDb(reservation.getRestaurantId());
        if(!isRestaurantPresentInDb){

            System.out.println("RESTAURANT-ID NOT FOUND IN DB");
            throw new RuntimeException("RESTAURANT-ID NOT FOUND IN DB");
        }

        if(reservation.getReservationId() == null || reservation.getReservationId() == "" || reservation.getReservationId() == " "){

            System.out.println("PASS PROPER RESERVATION-ID");
            throw new RuntimeException("RESERVATION-ID IMPROPER");
        }
        reservation.setReservationRequestStatus(ReservationRequestStatus.REJECTED);
        reservationRepository.save(reservation);

        smsService.sendMessageToCustomer(reservation.getCustomerPhone(), messageContent);

        return reservation;
    }

}
