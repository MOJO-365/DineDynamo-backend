package com.dinedynamo.services;

import com.dinedynamo.collections.Table;
import com.dinedynamo.collections.Reservation;
import com.dinedynamo.collections.Restaurant;
import com.dinedynamo.repositories.TableReservationRepository;
import com.dinedynamo.repositories.RestaurantRepository;
import com.dinedynamo.repositories.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
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
        if(reservation.getReservationTime() == null ||
                reservation.getCustomerName() == null ||
                reservation.getCustomerPhone() == null ||
                reservation.getGuestCount() == 0 ||
                reservation.getRestaurantId() == null ||
                reservation.getDineInTime() == null
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

        boolean isRestaurantAvailable = isRestaurantAvailable(reservation.getRestaurantId(),reservation.getDineInTime());

        Table table = isTableAvailable(reservation.getRestaurantId(), reservation.getGuestCount(),reservation.getDineInTime());


        if(!isRestaurantAvailable || table == null){
            return false;
        }


        reservation.setReservationTime(new Date());
        reservation.setTableId(table.getTableId());
        tableReservationRepository.save(reservation);
        return true;
    }



    public Table isTableAvailable(String restaurantId, int guestCount, Date dineInTime){


        //fetch all the tables of this restaurantId and capacity equal to guestCount.
        List<Table> tables = tableRepository.findByRestaurantIdAndCapacity(restaurantId,guestCount);


        //If table of requested guestCount is not present in DB, apply merge logic
        if(tables.isEmpty()){
            // merge logic
            System.out.println("TABLE WITH THIS CAPACITY IS NOT AVAILABLE BUT RESERVATION IS PROVIDED(Merge logic not applied)");
            System.out.println("EMPTY TABLE OBJECT WILL BE SAVED FOR THIS RESERVATION");
            return new Table();
        }



        //check if this table is present in 'reservations' collection.
        for(Table table: tables){

            boolean isPresentInReservations = isPresentInReservations(table.getTableId(), dineInTime);

            if(!isPresentInReservations){

                return table;
            }

        }

        //False will be returned if no merge is possible or no table is available in the restaurant
        //if the requested reservation has the same dineInTime as that of already existing table, reject the reservation request
        System.out.println("THE TABLES OF THIS CAPACITY ARE ALREADY RESERVED FOR THIS TIME");
        return null;
    }


    public boolean isRestaurantAvailable(String restaurantId, Date dineInTime){

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);

        if(restaurant.getStartTime().isAfter(LocalTime.ofSecondOfDay(dineInTime.getSeconds())) || restaurant.getEndTime().isBefore(LocalTime.ofSecondOfDay(dineInTime.getSeconds()))){
            return false;
        }


        return true;

    }


    public boolean isPresentInReservations(String tableId, Date dineInTime){

        Reservation reservation = tableReservationRepository.findByTableIdAndDineInTime(tableId,dineInTime).orElse(null);

        if(reservation == null){
            return true;
        }

        return false;

    }


}
