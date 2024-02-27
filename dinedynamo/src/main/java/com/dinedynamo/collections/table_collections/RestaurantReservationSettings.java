package com.dinedynamo.collections.table_collections;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document("reservation_settings")
public class RestaurantReservationSettings
{

    @Id
    String reservationSettingsId;

    @Indexed(unique = true)
    String restaurantId;

    String termsAndConditionsForTableReservation;

    String slotDuration;

    LocalTime firstSlotStartTime;

    LocalTime firstSlotEndTime;

    LocalTime secondSlotStartTime;

    LocalTime secondSlotEndTime;



}
