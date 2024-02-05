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

    public boolean validateReservationRequest(Reservation reservation){
        if(reservation.getCustomerName() == null ||
                reservation.getCustomerPhone() == null ||
                reservation.getGuestCount() == 0 ||
                reservation.getRestaurantId() == null ||
                reservation.getDineInDateAndTime() == null || reservation.getReservationTimeAndDate() == null
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

        Table table = isTableAvailable(reservation.getRestaurantId(), reservation.getGuestCount(),reservation.getDineInDateAndTime());

        if(table == null){

            System.out.println("TABLE WITH THIS CAPACITY NOT AVAILABLE");
            return false;
        }


        reservation.setTableId(table.getTableId());
        tableReservationRepository.save(reservation);
        return true;
    }


    public Table isTableAvailable(String restaurantId, int guestCount,String dineInDateAndTIme){


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

        return null;
    }



    public boolean isPresentInReservations(String tableId,String dineInDateAndTime){

        Reservation reservation = tableReservationRepository.findByTableIdAndDineInDateAndTime(tableId,dineInDateAndTime).orElse(null);


        if(reservation == null){

            return false;
        }

        System.out.println("TABLES ARE ALREADY RESERVED FOR THIS TIME AS GUEST COUNT");
        return true;

    }


}
