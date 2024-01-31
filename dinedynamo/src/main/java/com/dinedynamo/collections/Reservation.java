package com.dinedynamo.collections;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
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

    LocalDateTime dineInDateAndTime;

    Date reservationTimeAndDate;


    int guestCount;

    String tableId;

}
