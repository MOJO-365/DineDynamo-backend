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
        if(reservation.getReservationTimeAndDate() == null ||
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

        boolean isRestaurantAvailable = isRestaurantAvailable(reservation.getRestaurantId(),reservation.getDineInDate(),reservation.getDineInTime());

        System.out.println("isRestaurantAvailable: "+isRestaurantAvailable);
        Table table = isTableAvailable(reservation.getRestaurantId(), reservation.getGuestCount(),reservation.getDineInDate(),reservation.getDineInTime());


        if(!isRestaurantAvailable || table == null){

            System.out.println("TABLE WITH THIS CAPACITY NOT AVAILABLE, BUT RESERVATION WILL BE DONE (After merge logic)");
            return true;
        }


        reservation.setReservationTimeAndDate(new Date());
        reservation.setTableId(table.getTableId());
        tableReservationRepository.save(reservation);
        return true;
    }



    public Table isTableAvailable(String restaurantId, int guestCount,Date dineInDate ,Date dineInTime){


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

            boolean isPresentInReservations = isPresentInReservations(table.getTableId(),dineInDate ,dineInTime);

            if(!isPresentInReservations){

                return table;
            }

        }

        //False will be returned if no merge is possible or no table is available in the restaurant
        //if the requested reservation has the same dineInTime as that of already existing table, reject the reservation request
        System.out.println("THE TABLES OF THIS CAPACITY ARE ALREADY RESERVED FOR THIS TIME");
        return null;
    }


    public boolean isRestaurantAvailable(String restaurantId, Date dineInDate, Date dineInTime){

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);

        if(dineInDate.getDate() == (new Date().getDate()) ||  dineInDate.after(new Date())){
            if(restaurant.getStartTime().isBefore(LocalTime.ofSecondOfDay(dineInTime.getTime())) && restaurant.getEndTime().isAfter(LocalTime.ofSecondOfDay(dineInTime.getTime()))){

                System.out.println("TIME IS APT, RESTAURANT AVAILABLE");
                return true;
            }
        }


        System.out.println("TIME OR DATE OF RESERVATION IS NOT APPROPRIATE AS PER RESTAURANT START-END TIME");
        return false;

    }


    public boolean isPresentInReservations(String tableId,Date dineInDate ,Date dineInTime){

        Reservation reservation = tableReservationRepository.findByTableIdAndDineInDateAndDineInTime(tableId,dineInDate,dineInTime).orElse(null);


        if(reservation == null){

            return true;
        }

        System.out.println("TABLE ALREADY RESERVED");
        return false;

    }


}
