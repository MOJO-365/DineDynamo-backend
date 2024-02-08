package com.dinedynamo.services;


import com.dinedynamo.collections.Reservation;
import com.dinedynamo.collections.ReservationRequestStatus;
import com.dinedynamo.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerReservationService
{
    @Autowired
    ReservationRepository reservationRepository;

    public  boolean validateReservationRequest(Reservation reservation){

        if(reservation.getGuestCount() == 0 || reservation.getCustomerPhone() == null ||
        reservation.getCustomerName() == null || reservation.getReservationDateAndTime() == null ||
                reservation.getDineInDateAndTime() == null || reservation.getRestaurantId() == null ||
                reservation.getCustomerPhone() == ""  || reservation.getCustomerName() == "" || reservation.getDineInDateAndTime() == "" ||
                reservation.getReservationDateAndTime() == ""
        ){
            return false;
        }

        return true;

    }


    public Reservation save(Reservation reservation){


        reservation.setReservationRequestStatus(ReservationRequestStatus.HOLD);
        reservationRepository.save(reservation);
        return reservation;
    }
}
