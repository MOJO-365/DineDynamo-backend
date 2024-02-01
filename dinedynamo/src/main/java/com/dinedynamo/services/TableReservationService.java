package com.dinedynamo.services;

import com.dinedynamo.collections.Table;
import com.dinedynamo.collections.Reservation;
import com.dinedynamo.collections.Restaurant;
import com.dinedynamo.repositories.TableReservationRepository;
import com.dinedynamo.repositories.RestaurantRepository;
import com.dinedynamo.repositories.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class TableReservationService
{

    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    TableReservationRepository tableReservationRepository;
    @Autowired
    TableRepository tableRepository;

    boolean validateReservationRequest(Reservation reservation){
        if(reservation.getReservationTimeAndDate() == null ||
                reservation.getCustomerName() == null ||
                reservation.getCustomerPhone() == null ||
                reservation.getGuestCount() == 0 ||
                reservation.getRestaurantId() == null ||
                reservation.getDineInDateAndTime() == null
        ){
            return false;
        }


        return true;
    }


    /**
     *
     * @param reservation
     * @return true if table if reserved(saved in db), false if unable to reserve the table
     */
    public boolean save(Reservation reservation){

        boolean isRestaurantAvailable = isRestaurantAvailable(reservation.getRestaurantId(),reservation.getDineInDateAndTime());

        //System.out.println("isRestaurantAvailable: "+isRestaurantAvailable);
        Table table = isTableAvailable(reservation.getRestaurantId(), reservation.getGuestCount(),reservation.getDineInDateAndTime());


        if(!isRestaurantAvailable){
            System.out.println("RESTAURANT NOT AVAILABLE");
            return false;
        }

        if(table == null){

            System.out.println("TABLE WITH THIS CAPACITY NOT AVAILABLE");
            return false;
        }


        reservation.setReservationTimeAndDate(new Date());
        reservation.setTableId(table.getTableId());
        tableReservationRepository.save(reservation);
        return true;
    }



    public Table isTableAvailable(String restaurantId, int guestCount,LocalDateTime dineInDateAndTIme){


        //fetch all the tables of this restaurantId and capacity equal to guestCount.
        List<Table> tables = tableRepository.findByRestaurantIdAndCapacity(restaurantId,guestCount);



        //If table of requested guestCount is not present in DB, apply merge logic
        if(tables.isEmpty()){
            // merge logic
           return null;
        }



        //check if this table is present in 'reservations' collection.
        for(Table table: tables){

            boolean isPresentInReservations = isPresentInReservations(table.getTableId(),dineInDateAndTIme);

            if(!isPresentInReservations){

                return table;
            }

        }

        //False will be returned if no merge is possible or no table is available in the restaurant
        //if the requested reservation has the same dineInTime as that of already existing table, reject the reservation request

        return null;
    }


    public boolean isRestaurantAvailable(String restaurantId, LocalDateTime dineInDateAndTIme){

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);

        LocalDate currentDate = LocalDate.now();

        LocalDateTime localStartDateTimeOfRestaurant = LocalDateTime.of(currentDate,restaurant.getStartTime());
        LocalDateTime localEndDateTimeOfRestaurant = LocalDateTime.of(dineInDateAndTIme.toLocalDate(),restaurant.getEndTime());
        if(localStartDateTimeOfRestaurant.isBefore(dineInDateAndTIme) && localEndDateTimeOfRestaurant.isAfter(dineInDateAndTIme)){

            System.out.println("TIME IS APT, RESTAURANT AVAILABLE");
            return true;
        }



        System.out.println("TIME OF RESERVATION IS NOT APPROPRIATE AS PER RESTAURANT START-END TIME");
        return false;

    }


    public boolean isPresentInReservations(String tableId,LocalDateTime dineInDateAndTime){

        Reservation reservation = tableReservationRepository.findByTableIdAndDineInDateAndTime(tableId,dineInDateAndTime).orElse(null);


        if(reservation == null){

            return true;
        }

        System.out.println("TABLES ARE ALREADY RESERVED FOR THIS TIME AS GUEST COUNT");
        return false;

    }


}
