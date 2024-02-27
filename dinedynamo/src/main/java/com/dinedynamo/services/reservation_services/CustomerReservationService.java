package com.dinedynamo.services.reservation_services;


import com.dinedynamo.collections.table_collections.Reservation;
import com.dinedynamo.collections.table_collections.RestaurantReservationSettings;
import com.dinedynamo.helper.DateTimeUtility;
import com.dinedynamo.repositories.reservation_repositories.ReservationRepository;
import com.dinedynamo.repositories.reservation_repositories.RestaurantReservationSettingsRepository;
import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
        reservation.getCustomerName() == null || reservation.getDineInDate() == null || reservation.getRestaurantId() == null ||
                reservation.getCustomerPhone() == ""  || reservation.getCustomerName() == ""
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

        reservation.setReservationDateAndTime(LocalDateTime.now());

        if(reservation.getDineInDate().isBefore(LocalDate.now())){
            System.out.println("DATE OF RESERVATION IMPROPER");
            reservation.setReservationRequestStatus(Reservation.ReservationRequestStatus.INVALID);
            return reservation;
        }

//        if(dateTimeUtility.convertJSLocalStringToLocalDateTime(reservation.getDineInDateAndTime()).isBefore(LocalDateTime.now())){
//
//            System.out.println("DATE OF RESERVATION IMPROPER");
//            reservation.setReservationRequestStatus(Reservation.ReservationRequestStatus.INVALID);
//            return reservation;
//        }


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

        LocalTime firstSlotStartTime = restaurantReservationSettings.getFirstSlotStartTime();

        LocalTime firstSlotEndTime = restaurantReservationSettings.getFirstSlotEndTime();


//        LocalDateTime firstSlotStartLocalDateTime = dateTimeUtility.convertJSLocalStringToLocalDateTime(restaurantReservationSettings.getFirstSlotStartTime());
//
//        LocalDateTime firstSlotEndLocalDateTime = dateTimeUtility.convertJSLocalStringToLocalDateTime(restaurantReservationSettings.getFirstSlotEndTime());



        if(existingReservations == null){
            System.out.println("not existing in slot 1 = empty");
            return false;

        }

        for(Reservation reservationObj: existingReservations){

//            LocalDate dineInDateOfExistingObj = reservationObj.getDineInDate();
//
//            LocalTime dineInTimeOfExistingObj = reservationObj.getDineInTime();
            //String dateStringOfDineInDateAndTime = reservationObj.getDineInDateAndTime();


            //LocalDateTime localDateTimeDineInDateAndTime = dateTimeUtility.convertJSLocalStringToLocalDateTime(dateStringOfDineInDateAndTime);


            if(reservationObj.getDineInDate().equals(reservation.getDineInDate()) &&
            reservationObj.getDineInTime().isAfter(firstSlotStartTime) && reservationObj.getDineInTime().isBefore(firstSlotEndTime)
            && reservationObj.getReservationRequestStatus()!= Reservation.ReservationRequestStatus.CANCELLED
            ){
                System.out.println("CANNOT HAVE 2 BOOKINGS IN ONE SLOT");
                return true;
            }
//            if(reservationObj.getDineInDate().equals(dateTimeUtility.convertJSLocalStringToLocalDateTime(reservation.getDineInDateAndTime()).toLocalDate())
//            && localDateTimeDineInDateAndTime.toLocalTime().isAfter(firstSlotStartTime) && localDateTimeDineInDateAndTime.toLocalTime().isBefore(firstSlotEndTime)
//            && reservationObj.getReservationRequestStatus()!= Reservation.ReservationRequestStatus.CANCELLED){
//                System.out.println("CANNOT HAVE 2 BOOKINGS IN ONE SLOT");
//                return true;
//            }

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


        LocalTime secondSlotStartTime = restaurantReservationSettings.getSecondSlotStartTime();

        LocalTime secondSlotEndTime = restaurantReservationSettings.getSecondSlotEndTime();
//        LocalDateTime secondSlotStartLocalDateTime = dateTimeUtility.convertJSLocalStringToLocalDateTime(restaurantReservationSettings.getSecondSlotStartTime());
//
//        LocalDateTime secondSlotEndLocalDateTime = dateTimeUtility.convertJSLocalStringToLocalDateTime(restaurantReservationSettings.getSecondSlotEndTime());
//

        if(existingReservations == null){
            System.out.println("not existing in slot 2 = empty");
            return false;

        }

        for(Reservation reservationObj: existingReservations){

            //String dateStringOfDineInDateAndTime = reservationObj.getDineInDateAndTime();


            //LocalDateTime localDateTimeDineInDateAndTime = dateTimeUtility.convertJSLocalStringToLocalDateTime(dateStringOfDineInDateAndTime);

            if(reservationObj.getDineInDate().equals(reservation.getDineInDate()) &&
                    reservationObj.getDineInTime().isAfter(secondSlotStartTime) && reservationObj.getDineInTime().isBefore(secondSlotEndTime)
                    && reservationObj.getReservationRequestStatus()!= Reservation.ReservationRequestStatus.CANCELLED
            ){
                System.out.println("CANNOT HAVE 2 BOOKINGS IN ONE SLOT");
                return true;
            }


//            if(localDateTimeDineInDateAndTime.toLocalDate().equals(dateTimeUtility.convertJSLocalStringToLocalDateTime(reservation.getDineInDateAndTime()).toLocalDate())
//            && localDateTimeDineInDateAndTime.toLocalTime().isAfter(secondSlotStartTime) && localDateTimeDineInDateAndTime.toLocalTime().isBefore(secondSlotEndTime)
//            && reservationObj.getReservationRequestStatus()!= Reservation.ReservationRequestStatus.CANCELLED
//            ){
//                return true;
//            }
        }

        return false;

    }


    /**
     * returns the slot of corresponding reservation request
     */
    public int getSlot(Reservation reservation){

        RestaurantReservationSettings restaurantReservationSettings = restaurantReservationSettingsRepository.findByRestaurantId(reservation.getRestaurantId()).orElse(null);

        LocalTime requestedLocalTime = reservation.getDineInTime();

        //String firstSlotStartLocalDateTimeString = restaurantReservationSettings.getFirstSlotStartTime();
        LocalTime firstSlotStartLocalTime = restaurantReservationSettings.getFirstSlotStartTime();

        //String firstSlotEndLocalDateTimeString = restaurantReservationSettings.getFirstSlotEndTime();
        LocalTime firstSlotEndLocalTime = restaurantReservationSettings.getFirstSlotEndTime();

        if(firstSlotStartLocalTime!=null && firstSlotEndLocalTime!=null){
            if(requestedLocalTime.isBefore(firstSlotEndLocalTime) && requestedLocalTime.isAfter(firstSlotStartLocalTime)){
                return 1;
            }
        }

        //String secondSlotStartLocalDateTimeString = restaurantReservationSettings.getSecondSlotStartTime();
        LocalTime secondSlotStartLocalTime = restaurantReservationSettings.getSecondSlotStartTime();

        //String secondSlotEndLocalDateTimeString = restaurantReservationSettings.getSecondSlotEndTime();
        LocalTime secondSlotEndLocalTime = restaurantReservationSettings.getSecondSlotEndTime();

        if(secondSlotEndLocalTime!=null && secondSlotStartLocalTime!=null){
            if(requestedLocalTime.isBefore(secondSlotEndLocalTime) && requestedLocalTime.isAfter(secondSlotStartLocalTime)){
                return 2;

            }
        }
        return -1;

    }

}
