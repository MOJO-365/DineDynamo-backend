package com.dinedynamo.collections;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

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

    String reservationTimeAndDate;  //Time when customer requests for reservation

    int guestCount;

    String tableId;



}
