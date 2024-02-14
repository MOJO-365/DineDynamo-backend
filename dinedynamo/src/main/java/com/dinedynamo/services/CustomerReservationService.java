package com.dinedynamo.services;


import com.dinedynamo.collections.Reservation;
import com.dinedynamo.collections.RestaurantReservationSettings;
import com.dinedynamo.helper.DateTimeUtility;
import com.dinedynamo.repositories.ReservationRepository;
import com.dinedynamo.repositories.RestaurantReservationSettingsRepository;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CustomerReservationService
{
    @Autowired
    DateTimeUtility dateTimeUtility;


    @Autowired
    MongoClient mongoClient;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    RestaurantReservationSettingsRepository restaurantReservationSettingsRepository;

    /**
     *
     * @param reservation
     * @return boolean
     * checks if the attributes are non-empty
     */
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


    /**
     *
     * @param reservation
     * @return reservation
     * checks the constraints for saving the reservation request into database. If the constraints are satisfied, object will be saved are reservation is returned, else null is returned
     */
    public Reservation save(Reservation reservation){


        //System.out.println("---->"+reservationRepository.findByRestaurantIdAndCustomerPhone(reservation.getReservationId(),reservation.getCustomerPhone()));
        if(dateTimeUtility.convertJSLocalStringToLocalDateTime(reservation.getDineInDateAndTime()).isBefore(LocalDateTime.now())){

            System.out.println("DATE OF RESERVATION IMPROPER");
            reservation.setReservationRequestStatus(Reservation.ReservationRequestStatus.INVALID);
            return reservation;
        }


        int slot = getSlot(reservation);
        if(slot == -1){
            throw new RuntimeException("SLOT TIMINGS INCORRECT");
        }


        if(slot == 1){
            boolean isPresentInFirstSlot = isPresentInFirstSlot(reservation);
            System.out.println("REQUESTED SLOT: 1");

            if(isPresentInFirstSlot){
                reservation.setReservationRequestStatus(Reservation.ReservationRequestStatus.INVALID);
                return reservation;
            }
        }

        if(slot == 2){
            System.out.println("REQUESTED SLOT: 2");
            boolean isPresentInSecondSlot = isPresentInSecondSlot(reservation);
            if(isPresentInSecondSlot){
                reservation.setReservationRequestStatus(Reservation.ReservationRequestStatus.INVALID);
                return reservation;
            }

        }


        reservation.setReservationRequestStatus(Reservation.ReservationRequestStatus.HOLD);
        reservationRepository.save(reservation);
        return reservation;
    }


    /**
     *
     * checks if the reservation requests' dineIn time is within first slot or not
     */
    public boolean isPresentInFirstSlot(Reservation reservation){

        List<Reservation> existingReservations = reservationRepository.findByRestaurantIdAndCustomerPhone(reservation.getRestaurantId(), reservation.getCustomerPhone()).orElse(null);

        RestaurantReservationSettings restaurantReservationSettings = restaurantReservationSettingsRepository.findByRestaurantId(reservation.getRestaurantId()).orElse(null);

        LocalDateTime firstSlotStartLocalDateTime = dateTimeUtility.convertJSLocalStringToLocalDateTime(restaurantReservationSettings.getFirstSlotStartTime());

        LocalDateTime firstSlotEndLocalDateTime = dateTimeUtility.convertJSLocalStringToLocalDateTime(restaurantReservationSettings.getFirstSlotEndTime());



        if(existingReservations == null){
            System.out.println("not existing in slot 1 = empty");
            return false;

        }

        for(Reservation reservationObj: existingReservations){


            String dateStringOfDineInDateAndTime = reservationObj.getDineInDateAndTime();


            LocalDateTime localDateTimeDineInDateAndTime = dateTimeUtility.convertJSLocalStringToLocalDateTime(dateStringOfDineInDateAndTime);

            if(localDateTimeDineInDateAndTime.toLocalDate().equals(dateTimeUtility.convertJSLocalStringToLocalDateTime(reservation.getDineInDateAndTime()).toLocalDate())
            && localDateTimeDineInDateAndTime.toLocalTime().isAfter(firstSlotStartLocalDateTime.toLocalTime()) && localDateTimeDineInDateAndTime.toLocalTime().isBefore(firstSlotEndLocalDateTime.toLocalTime())
            ){
                System.out.println("CANNOT HAVE 2 BOOKINGS IN ONE SLOT");
                return true;
            }

        }

        return false;

    }


    /**
     *
     * checks if the reservation requests' dineIn time is within second slot or not
     */
    public boolean isPresentInSecondSlot(Reservation reservation){


        List<Reservation> existingReservations = reservationRepository.findByRestaurantIdAndCustomerPhone(reservation.getRestaurantId(), reservation.getCustomerPhone()).orElse(null);

        RestaurantReservationSettings restaurantReservationSettings = restaurantReservationSettingsRepository.findByRestaurantId(reservation.getRestaurantId()).orElse(null);

        LocalDateTime secondSlotStartLocalDateTime = dateTimeUtility.convertJSLocalStringToLocalDateTime(restaurantReservationSettings.getSecondSlotStartTime());

        LocalDateTime secondSlotEndLocalDateTime = dateTimeUtility.convertJSLocalStringToLocalDateTime(restaurantReservationSettings.getSecondSlotEndTime());


        if(existingReservations == null){
            System.out.println("not existing in slot 2 = empty");
            return false;

        }

        for(Reservation reservationObj: existingReservations){

            String dateStringOfDineInDateAndTime = reservationObj.getDineInDateAndTime();

            LocalDateTime localDateTimeDineInDateAndTime = dateTimeUtility.convertJSLocalStringToLocalDateTime(dateStringOfDineInDateAndTime);

            if(localDateTimeDineInDateAndTime.toLocalDate().equals(dateTimeUtility.convertJSLocalStringToLocalDateTime(reservation.getDineInDateAndTime()).toLocalDate())
            && localDateTimeDineInDateAndTime.toLocalTime().isAfter(secondSlotStartLocalDateTime.toLocalTime()) && localDateTimeDineInDateAndTime.toLocalTime().isBefore(secondSlotEndLocalDateTime.toLocalTime())
            ){
                return true;
            }
        }

        return false;

    }


    /**
     * returns the slot of corresponding reservation request
     */
    public int getSlot(Reservation reservation){

        RestaurantReservationSettings restaurantReservationSettings = restaurantReservationSettingsRepository.findByRestaurantId(reservation.getRestaurantId()).orElse(null);

        LocalDateTime firstSlotStartLocalDateTime = dateTimeUtility.convertJSLocalStringToLocalDateTime(restaurantReservationSettings.getFirstSlotStartTime());

        LocalDateTime firstSlotEndLocalDateTime = dateTimeUtility.convertJSLocalStringToLocalDateTime(restaurantReservationSettings.getFirstSlotEndTime());

        LocalDateTime requestedLocalDateTime = dateTimeUtility.convertJSLocalStringToLocalDateTime(reservation.getDineInDateAndTime());

        LocalDateTime secondSlotStartLocalDateTime = dateTimeUtility.convertJSLocalStringToLocalDateTime(restaurantReservationSettings.getSecondSlotStartTime());

        LocalDateTime secondSlotEndLocalDateTime = dateTimeUtility.convertJSLocalStringToLocalDateTime(restaurantReservationSettings.getSecondSlotEndTime());

        if(requestedLocalDateTime.toLocalTime().isBefore(firstSlotEndLocalDateTime.toLocalTime()) && requestedLocalDateTime.toLocalTime().isAfter(firstSlotStartLocalDateTime.toLocalTime())){
            return 1;
        }
        if(requestedLocalDateTime.toLocalTime().isBefore(secondSlotEndLocalDateTime.toLocalTime()) && requestedLocalDateTime.toLocalTime().isAfter(secondSlotStartLocalDateTime.toLocalTime())){
            return 2;

        }
        return -1;

    }

}
