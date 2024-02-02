package com.dinedynamo.collections;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document("waitinglist")
public class WaitingList
{
    @Id
    String waitingId;

    String restaurantId;

    int guestCount;

    String customerPhone;

    String customerName;

    LocalDateTime reservationDateAndTime;   //This would be the time when customer requested reservation - saving in DB for setting priority (FCFS)



}
