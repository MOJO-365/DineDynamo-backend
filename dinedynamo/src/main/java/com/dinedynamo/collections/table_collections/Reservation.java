package com.dinedynamo.collections.table_collections;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document("reservations")
public class Reservation {

    @Id
    String reservationId;

    String restaurantId;

    String customerPhone;

    String customerName;

    String dineInDateAndTime;  //Time when cutomer will come to the restaurant

    String reservationDateAndTime;  //Time when customer requests for reservation

    int guestCount;



    ReservationRequestStatus reservationRequestStatus;

    public enum ReservationRequestStatus
    {
        HOLD,REJECTED,ACCEPTED,INVALID,CANCELLED
    }
}
