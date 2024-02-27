package com.dinedynamo.services.reservation_services;

import com.dinedynamo.collections.table_collections.Reservation;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.helper.DateTimeUtility;
import com.dinedynamo.repositories.reservation_repositories.ReservationRepository;
import com.dinedynamo.repositories.restaurant_repositories.RestaurantRepository;
import com.dinedynamo.services.restaurant_services.RestaurantService;
import com.dinedynamo.services.external_services.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


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

    @Autowired
    DateTimeUtility dateTimeUtility;


    /**
     *
     * @param reservation
     * @param messageContent
     * @return reservation object that has been accepted by the restaurant
     * This api helps restaurant owner to accept the HOLD reservation request. As the request is accepted, text message will be sent to the customer
     */
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

        reservation.setReservationRequestStatus(Reservation.ReservationRequestStatus.ACCEPTED);
        reservationRepository.save(reservation);

        System.out.println("Ready to send sms");
        smsService.sendMessageToCustomer(reservation.getCustomerPhone(),messageContent);
        return reservation;
    }


    /**
     *
     * @param reservation
     * @param messageContent
     * @return Reservation object that is passed in request and rejected by the restaurant
     * If the request is rejected, the reservation request status is changed to 'REJECTED' as text message is sent to customer
     *
     */
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
        reservation.setReservationRequestStatus(Reservation.ReservationRequestStatus.REJECTED);
        reservationRepository.save(reservation);

        smsService.sendMessageToCustomer(reservation.getCustomerPhone(), messageContent);

        return reservation;
    }


    /**
     *
     * @param restaurantId
     * @return boolean
     * Once the dineInDateAndTime of Reservation object is after the current date and time, the record will be deleted from the database
     * Main purpose: clearing the collection
     */
    public boolean clearOldReservationsFromDb(String restaurantId){
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);

        if(restaurant == null){
            throw new RuntimeException("Restaurant not present in database");
        }

        List<Reservation> listOfReservations = reservationRepository.findByRestaurantId(restaurant.getRestaurantId()).orElse(null);

        for(Reservation reservation: listOfReservations){

            LocalDate dineInDate = reservation.getDineInDate();

            //LocalDateTime dineInDateAndTime = dateTimeUtility.convertJSLocalStringToLocalDateTime(dineInDateAndTimeString);

            LocalDate today = LocalDate.now();

            if(today.isAfter(dineInDate)){
                reservationRepository.delete(reservation);
            }

        }

        return true;
    }


    /**
     *
     * @param reservation
     * @return
     * Once the reservaiton is cancelled from the customer side, the status of that reservation request changes into 'CANCELLED' in the database.
     * This method helps deleting the cancelled reservations from the database
     */
    public Reservation deleteCancelledReservation(Reservation reservation){

        reservation = reservationRepository.findById(reservation.getReservationId()).orElse(null);

        if(reservation == null){

            System.out.println("RESERVATION-ID DOES NOT EXIST IN DATABASE, INCORRECT ID PASSED");
            return null;
        }

        else if(reservation.getReservationRequestStatus() == Reservation.ReservationRequestStatus.CANCELLED){
            reservationRepository.delete(reservation);
            System.out.println("RESERVATION DELETED FROM DATABASE");
            return reservation;
        }
        else{
            System.out.println("CANNOT DELETE RESERVATION, AS THE STATUS IS NOT 'CANCELLED' IN THE DATABASE");
            return null;
        }
    }

}
